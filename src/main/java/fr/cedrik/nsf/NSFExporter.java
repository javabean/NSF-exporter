/**
 *
 */
package fr.cedrik.nsf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipOutputStream;

import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.MIMEEntity;
import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.NotesThread;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.cedrik.inotes.MessageMetaData;
import fr.cedrik.inotes.fs.BaseZipWriter;

/**
 * @author C&eacute;drik LIME
 */
public class NSFExporter {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * List of '('-begining Views that are allowed for export
	 */
	// db.Views: [($Profiles), ($VIMPeople), (MiniView - Notices2), ($VIM98), ($POP3), ($Journal), (iNotes_Contacts), ($Drafts), (Mes contacts), ($MAPIUseContacts), ($Contacts), ($Calendar), ($ByGroupCategory), ($PeopleGroupsFlatExt), (iNotes_ArchiveTOC), ($MeetingsOnMyCalendar), (Modèle), ($FolderInfo), ($ICALInfoDocs), (Custom Expiration\Expired Documents), ($MAPIUseCalendar), ($FolderRefInfo), ($ByCategory), (Par catégorie), (iNotes_ToDo), (Agendas importés), ($ThreadsEmbed), ($ApptUNID), ($Inbox), ($MeetingsAllByDate2), (Custom Expiration\By Date), (iNotes_Notices), (iNotes), (MiniView - Tasks2), (Haiku_TasksAll), (Tâches en instance\Terminées), ($IMTranscripts), ($Alarms), ($VIM23), (Dates de naissance et anniversaires), (Mail Threads), (CalSummary), (Tâches en instance\Inachevées), (Règles), ($Sent), (recallReports), ($JunkMail), ($OfflineCalendar), ($VIM100), (Tâches en instance\Par catégorie), ($POP3UIDL), ($SoftDeletions), ($HeadlinesView), (By Person), ($Meetings), ($ToDo), (Tâches en instance\Groupe), (Groupe), ($RepeatLookup), (Custom Expiration\Manage Folders), ($VIM42), (Haiku_TOC), ($PeopleGroupsFlat), (iNotes_DraftCleanUp), ($All), (MiniView - Trash2), (Group Calendars), ($Inbox-Categorized1), ($MAPIUseCalReminders), ($FolderAllInfo), (MiniView - Followup2), (MiniView - Group Calendars), (iNotes_Stationery), ($Follow-Up), (Tâches en instance\Personnelles), ($Users), (Par société), ($VIM256), ($Trash), ($EntriesOnMyCalendarByCategory), ($CompanyAddressLookup), ($VIMGroups), ($STOnlineMeetingRooms), ($FolderHiddenPublic), (Recently Archived), (Drafts), (Sent), Archives, Archives\2012, Clients, Clients\Nissan, Archives\CVs, Clients\Nissan\YouPlus, Clients\Nissan\CQ5, Clients\Nissan\hosting, Clients\Nissan\hosting\Publicis, Clients\Nissan\hosting\BT, Clients\Nissan\hosting\CDN, Clients\Nissan\hosting\LBN, Clients\Nissan\Telematics, Clients\Nissan\UsedCars, Clients\Nissan\SSO, Clients\Nissan\BusinessContact, Clients\Nissan\Divers, Clients\BnB, Clients\Nissan\ESB, VinMalin]
	public static final List<String> ALLOWED_SYSTEM_VIEWS = Arrays.asList("($Inbox)", "($Drafts)", "($Sent)", "($Follow-Up)");//, "($All)"

//	public static final String DOCUMENT_From          = "From";//$NON-NLS-1$
//	public static final String DOCUMENT_SendTo        = "SendTo";//$NON-NLS-1$
//	public static final String DOCUMENT_CopyTo        = "CopyTo";//$NON-NLS-1$
//	public static final String DOCUMENT_BlindCopyTo   = "BlindCopyTo";//$NON-NLS-1$
//	public static final String DOCUMENT_Subject       = "Subject";//$NON-NLS-1$
//	public static final String DOCUMENT_Body          = "Body";//$NON-NLS-1$
//	public static final String DOCUMENT_DeliveredDate = "DeliveredDate";//$NON-NLS-1$
//	public static final String DOCUMENT_PostedDate    = "PostedDate";//$NON-NLS-1$

	protected ProgressListener progressListener;

	/**
	 *
	 */
	public NSFExporter(ProgressListener progressListener) {
		this.progressListener = progressListener;
	}

	/**
	 * @param notesPassword {@code null} for asking on Console
	 * @param platform {@code null} for current platform
	 * @param dbFileName {@code null} for current email database (usually in {@code ~/Library/Application Support/Lotus Notes Data/mail/})
	 * @param exportArchiveFileName {@code null} for built-in default in current directory
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws NotesException
	 */
	public void export(String notesPassword, Platform platform, String dbFileName, String exportArchiveFileName) throws FileNotFoundException, IOException, NotesException {
		try {
			NotesThread.sinitThread();
			Session session;
			if (notesPassword == null) {
				session = NotesFactory.createSessionWithFullAccess();
			} else {
//				session = NotesFactory.createSession((String)null, (String)null, notesPassword);
				session = NotesFactory.createSessionWithFullAccess(notesPassword);
			}
			log.info("NotesVersion\t: "+session.getNotesVersion());
			log.debug("Platform\t\t: " + session.getPlatform());
			log.info("CommonUserName\t: "+session.getCommonUserName());
			log.info("UserName\t\t: "+session.getUserName());
			log.info("EffectiveUserName\t: "+session.getEffectiveUserName());
			log.debug("isConvertMime\t: "+session.isConvertMime());
			log.debug("isConvertMIME\t: "+session.isConvertMIME());
			log.debug("isOnServer\t\t: "+session.isOnServer());
			log.debug("isRestricted\t: "+session.isRestricted());
			log.debug("isTrackMillisecInJavaDates\t: "+session.isTrackMillisecInJavaDates());
			log.debug("isTrustedSession\t: "+session.isTrustedSession());
			log.debug("isValid\t\t: "+session.isValid());
			session.setConvertMime(false);
			session.setConvertMIME(false);
			Database db;
			if (StringUtils.isEmpty(dbFileName)) {
				db = session.getDbDirectory(null).openMailDatabase();
			} else {
				if ( ! new File(dbFileName).canRead()) {
					throw new FileNotFoundException(dbFileName);
				}
				db = session.getDatabase(null, dbFileName, false); // Use this for opening archives!
			}
			if (db == null || ! db.isOpen()) {
				throw new FileNotFoundException(dbFileName);
			}
			log.info("db.title\t\t: "+db.getTitle());
			log.debug("db.fileName\t: "+db.getFileName());
			log.info("db.filePath\t: "+db.getFilePath());
			log.debug("db.NotesURL\t: "+db.getNotesURL());
			log.info("db.LastModified\t: "+db.getLastModified());

			int allDocumentsCount = db.getAllDocuments().getCount();
			log.info("db.allDocuments\t: " + allDocumentsCount);
			if (progressListener != null) {
				progressListener.setTotalNumberOfDocuments(allDocumentsCount);
			}

			if (platform == null) {
				platform = Platform.getInstance(session.getPlatform());
			}
			final String currentDate = DateFormatUtils.ISO_DATE_FORMAT.format(new Date());
			File exportArchiveFile;
			{
				final String defaultExportArchiveFileName = FilenameUtils.removeExtension(db.getFileName()) + '_' + platform.getMBoxTypeString() + '_'+currentDate+".zip";
				if (StringUtils.isBlank(exportArchiveFileName)) {
					exportArchiveFileName = defaultExportArchiveFileName;
				}
				exportArchiveFile = new File(exportArchiveFileName);
				if (exportArchiveFile.isDirectory()) {
					exportArchiveFile = new File(exportArchiveFile, defaultExportArchiveFileName);
				} else {
					if ( ! FilenameUtils.isExtension(exportArchiveFileName.toLowerCase(), "zip")) {
						exportArchiveFileName += ".zip";
					}
					exportArchiveFile = new File(exportArchiveFileName);
				}
			}
			ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(exportArchiveFile));
			outZip.setComment("Lotus Notes archive " + db.getFileName() + " for " + session.getCommonUserName() + " at " + DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(new Date()));
			BaseZipWriter writer = platform.getZipWriter(outZip, FilenameUtils.removeExtension(db.getFileName())+'_'+currentDate);

			for (View view : (Vector<View>) db.getViews()) {
				if (view.getName().startsWith("(") && !ALLOWED_SYSTEM_VIEWS.contains(view.getName())) {//TODO || ! view.isFolder()
					log.debug("Skipping view: {}", view.getName());
					continue;
				}
				exportView(view, writer, platform);
				view.recycle();
			}
			outZip.finish();
			outZip.close();

//			dumpDocument(db.getAllDocuments().getNthDocument(442));
//			WriteOutputMIME(db.getAllDocuments().getNthDocument(442), new File("/Users/limec/Desktop/tmp/Notes/"));

			db.recycle();
			session.recycle();
		} finally {
			NotesThread.stermThread();
		}
	}

	protected void exportView(View view, BaseZipWriter writer, Platform platform) throws NotesException, IOException {
		log.info("Exporting view \"{}\" ({} documents)", view.getName(), view.getEntryCount());
		log.debug("view.Aliases\t: " + view.getAliases());
		log.debug("view.isCalendar\t: " + view.isCalendar());
		log.debug("view.isFolder\t: " + view.isFolder());
		log.debug("view.NotesURL\t: " + view.getNotesURL());
		log.debug("view.UniversalID\t: " + view.getUniversalID());
		log.debug("view.FirstDocument\t: " + view.getFirstDocument());
		log.debug("view.LastDocument\t: " + view.getLastDocument());
		ViewEntryCollection allEntries = view.getAllEntries();
		log.debug("view entries: " + allEntries.getCount());

		if (allEntries.getCount() > 0) {
			ViewEntry viewEntry = allEntries.getFirstEntry();
			String folderChainName = encodeFolderName(view.getName()).replace('\\', '/');
			writer.openFolder(folderChainName);
			while (viewEntry != null) {
				if ( ! viewEntry.isValid() || ! viewEntry.isDocument()) {
					log.debug("Invalid ViewEntry or not a Document: " + viewEntry);
					continue;
				}
				log.trace("-- new ViewEntry -- " + viewEntry.getUniversalID());
				log.trace("viewEntry.ChildCount\t: " + viewEntry.getChildCount());
				log.trace("viewEntry.DescendantCount\t: " + viewEntry.getDescendantCount());
				log.trace("viewEntry.isCategory\t: " + viewEntry.isCategory());
				log.trace("viewEntry.isConflict\t: " + viewEntry.isConflict());
				log.trace("viewEntry.isDocument\t: " + viewEntry.isDocument());
				log.trace("viewEntry.isTotal\t: " + viewEntry.isTotal());
				log.trace("viewEntry.isValid\t: " + viewEntry.isValid());
				Document document = viewEntry.getDocument();
				exportDocument(document, writer, folderChainName);
				document.recycle();
				ViewEntry veBackup = viewEntry;
				viewEntry = allEntries.getNextEntry();
				veBackup.recycle();
			}
			writer.closeFolder();
		}
	}

	protected void exportDocument(Document document, BaseZipWriter writer, String folderChainStr) throws NotesException, IOException {
		if (document.isDeleted()) {
			log.debug("Skipping deleted document: ", document.getUniversalID());
			return;
		}
		log.info("Exporting document: {} | {} ({} bytes)", document.getNoteID(), document.getUniversalID(), document.getSize());
		log.trace("document.NameOfProfile\t: " + document.getNameOfProfile());
		log.debug("document.isDeleted\t: " + document.isDeleted());
		log.debug("document.isEncrypted\t: " + document.isEncrypted());
		log.debug("document.isSigned\t: " + document.isSigned());
		log.trace("document.isProfile\t: " + document.isProfile());
//		for (Item item : (Vector<Item>) document.getItems()) {
//			log.trace("== new item ==");
//			log.trace("item.Name\t: " + item.getName());
//			log.trace("item.Type\t: " + item.getType());
//			log.trace("item.isAuthors\t: " + item.isAuthors());
//			log.trace("item.isEncrypted\t: " + item.isEncrypted());
//			log.trace("item.isNames\t: " + item.isNames());
//			log.trace("item.isReaders\t: " + item.isReaders());
//			log.trace("item.isSigned\t: " + item.isSigned());
//			log.trace("item.Text\t: " + item.getText());
////			log.trace("item.Values\t: " + item.getValues());
//		}
		MessageMetaData message = new MessageMetaData();
		message.unid   = document.getUniversalID();
		message.noteid = document.getNoteID();
		message.size   = document.getSize();
		message.date   = lnDataTime2Date(document.getCreated());//FIXME 1. document.getItemValue(DOCUMENT_PostedDate)  or  2. document.getItemValue(DOCUMENT_DeliveredDate)
		int expectedMIMEsize = Math.max(16384, (int)(document.getSize()*1.37-8000)); // linear regression tests MIME / Notes document size
		StringWriter buff;
		if (expectedMIMEsize <= SMALL_MESSAGES_BUFFER.getBuffer().capacity() / 2) {
			buff = SMALL_MESSAGES_BUFFER;
			buff.getBuffer().setLength(0);
		} else {
			buff = new StringWriter(expectedMIMEsize);
		}
		if (! writeOutputMIME(document, buff)) {
			log.error("Can not convert document {} | {} to MIME; skipping", message.noteid, message.unid);
			return;
		}
		Iterator<String> mime = new WriterToIteratorAdaptor(buff);
		try {
			writer.openFile(folderChainStr, message);
			writer.write(message, mime);
		} finally {
			writer.closeFile(message);
			if (progressListener != null) {
				progressListener.documentExported(message.noteid);
			}
		}
	}
	/**
	 * Re-usable StringWriter cache for "small" messages, to avoid creating 1000s of short-lived objects
	 */
	private static final StringWriter SMALL_MESSAGES_BUFFER = new StringWriter(512*1024);

//	@Deprecated
//	public static void getMIME_ManualVersion(Document document) throws NotesException {
//		StringBuilder rfc5322 = new StringBuilder(1024);
//		MIMEEntity mimeEntity = document.getMIMEEntity();
//		if (mimeEntity == null) {
//			document.removeItem("$KeepPrivate");//$NON-NLS-1$
//			document.convertToMIME(Document.CVT_RT_TO_PLAINTEXT_AND_HTML);
//			mimeEntity = document.getMIMEEntity();
//			System.out.println("document.MIMEEntity\t: " + mimeEntity);
//		}
//		Vector<MIMEHeader> mimeHeaders = mimeEntity.getHeaderObjects();
//		for (MIMEHeader mimeHeader : mimeHeaders) {
//			rfc5322.append(mimeHeader.getHeaderName() + ": " + mimeHeader.getHeaderValAndParams() + "\r\n");
//			mimeHeader.recycle();
//		}
//		// If you work through the Document you will see, that it has a child for every multipart part.
//		// So you have to make shure, you iterate over each child and add it to the buffer, too.
//		MIMEEntity childMime = mimeEntity.getFirstChildEntity();
//		while (childMime != null) {
//			//TODO
//			MIMEEntity nextChild = childMime.getNextSibling();
//			if (nextChild == null) {
//				nextChild = childMime.getParentEntity();
//				if (nextChild != null) {
//					nextChild = nextChild.getNextSibling();
//				}
//			}
//		}
//		// If you are working your way through attachments, you will find it usefull to have the
//		// public void encodeContent(int encoding) from the MIMEEntity to convert attachements to
//		// MIMEEntity.ENC_BASE64. Everything else will not work.
//		if (document.hasItem("$File")) {
//			for (Item item : (Vector<Item>) document.getItems()) {
//				if (item.getType() == Item.ATTACHMENT) {
//					String pAttachment = (String) item.getValues().get(0);
//					document.getAttachment(pAttachment).getFileSize();
//					document.getAttachment(pAttachment).extractFile("/tmp/"+pAttachment);
//				}
//			}
//		}
//		mimeEntity.recycle();
//	}

	protected boolean convertToMime(Document doc) throws NotesException {
		if (doc.getMIMEEntity("Body") == null) {//$NON-NLS-1$
			doc.removeItem("$KeepPrivate");//$NON-NLS-1$
			// choose between the 3 conversion alternatives, depending on RichTextItem presence
			boolean hasText = false;
			boolean hasRichText = false;
			for (Item item : (Vector<Item>) doc.getItems()) {
				switch (item.getType()) {
				case Item.TEXT:
					hasText = true;
					break;
				case Item.RICHTEXT:
					hasRichText = true;
					break;
				}
				item.recycle();
			}
			try {
				if (hasRichText) {
					if (hasText) {
						log.trace("Converting document {} to HTML and TEXT", doc.getUniversalID());
						doc.convertToMIME(Document.CVT_RT_TO_PLAINTEXT_AND_HTML);
					} else {
						log.trace("Converting document {} to HTML", doc.getUniversalID());
						doc.convertToMIME(Document.CVT_RT_TO_HTML);
					}
				} else {
					log.trace("Converting document {} to TEXT", doc.getUniversalID());
					doc.convertToMIME(Document.CVT_RT_TO_PLAINTEXT);
				}
			} catch (NotesException ne) {
				log.warn("Lotus Notes error while converting document {} to MIME: {}", doc.getNoteID(), ne.getLocalizedMessage());
			}
		}
		return doc.getMIMEEntity("Body") != null;
	}

	private void writeOutputMIME(Document doc, File outDir) throws NotesException, IOException {
		String noteId = doc.getNoteID();//doc.getUniversalID();
		File outFile = new File(outDir, noteId + ".eml");
		Writer output = new FileWriter(outFile);
		writeOutputMIME(doc, output);
	}
	protected boolean writeOutputMIME(Document doc, Writer output) throws NotesException, IOException {
		// access document as mime parts
		if (! convertToMime(doc)) {
			return false;
		}
		MIMEEntity mE = doc.getMIMEEntity("Body");//$NON-NLS-1$
		assert mE != null;

		try {
			String headers = mE.getHeaders();
			int encoding = mE.getEncoding();

			// message envelope. If no MIME-version header, add one
			if (headers.indexOf("MIME-Version:") < 0) {//$NON-NLS-1$
				output.write("MIME-Version: 1.0\n");//$NON-NLS-1$
			}
			output.write(headers);
			if (!headers.endsWith("\n")) {
				output.write("\n");
			}
//			Vector<MIMEHeader> mimeHeaders = mE.getHeaderObjects();
//			for (MIMEHeader mimeHeader : mimeHeaders) {
//				output.append(mimeHeader.getHeaderName() + ": " + mimeHeader.getHeaderValAndParams() + "\r\n");
//				mimeHeader.recycle();
//			}
//			output.write("\n");

			// for multipart, usually no main-msg content
			String content = mE.getContentAsText(); // includes Preamble
			if (content != null && content.trim().length() > 0) {
				output.write(content);
				output.write("\n");
			}

			// begin of main envelope
			output.write(mE.getBoundaryStart());

			// For multipart, examine each child entity,
			// re-code to base64 if necessary
			if (mE.getContentType().startsWith("multipart")) {//$NON-NLS-1$
				String preamble = mE.getPreamble();
				MIMEEntity mChild = mE.getFirstChildEntity();
				while (mChild != null) {
					// convert binary parts to base-64
					if (mChild.getEncoding() == MIMEEntity.ENC_IDENTITY_BINARY) {
						mChild.encodeContent(MIMEEntity.ENC_BASE64);
						// get Headers again, because changed
					}

					preamble = mChild.getPreamble();
					content = mChild.getBoundaryStart();
					output.write(content);
					if (!content.endsWith("\n")) {
						output.write("\n");
					}
					content = mChild.getHeaders(); // get after encoding content to B64, because changed
					output.write(content);
					if (!content.endsWith("\n")) {
						output.write("\n");
					}
					output.write("\n");

					content = mChild.getContentAsText(); // includes Preamble
					if (content != null && content.length() > 0) {
						output.write(content);
					}
					output.write(mChild.getBoundaryEnd());

					MIMEEntity oldChild = mChild;
					mChild = mChild.getNextSibling();
					oldChild.recycle();
				} // end while
			} // end multipart

			// end of main envelope
			output.write(mE.getBoundaryEnd());
		} finally {
			output.close();
		}

		return true;
	} // end WriteOutputMIME


	/**
	 * @see RFC2060 5.1.3.  Mailbox International Naming Convention  + special treatment for '/'
	 */
	protected static String encodeFolderName(String folderName) {
//		return BASE64MailboxEncoder.encode(folderName).replace("/", "&AC8-");
//		return StringUtils.replaceChars(folderName, "\\/:*?\"<>|", "_________");
		// '\' is used a folder delimiter in Lotus Notes
		return StringUtils.replaceChars(folderName, "/:*?\"<>|", "________");
	}

	public static Date lnDataTime2Date(DateTime dateTime) throws NotesException {
		Date jDate = dateTime.toJavaDate();

		// Fix for the DateTime object without timepart
		String timeOnly = dateTime.getTimeOnly();
		if (timeOnly == null || timeOnly.length() == 0) {
			Calendar jCalendar = Calendar.getInstance();
			jCalendar.setTime(jDate);
			jCalendar.set(Calendar.HOUR, 0);// TODO Calendar.HOUR_OF_DAY?
			jCalendar.set(Calendar.MINUTE, 0);
			jCalendar.set(Calendar.SECOND, 0);
			jCalendar.set(Calendar.MILLISECOND, 0);
			jDate = jCalendar.getTime();
		}
		return jDate;
	}
}
