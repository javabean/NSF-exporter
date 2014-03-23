package fr.cedrik.nsf;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;

/**
 * @author C&eacute;drik LIME
 */
class WriterToIteratorAdaptor implements Iterator<String> {
	protected BufferedReader in;
	protected String nextLine = null;

	public WriterToIteratorAdaptor(StringWriter buffer) {
		in = new BufferedReader(new StringReader(buffer.toString()));
		readNextLine();
	}

	@Override
	public boolean hasNext() {
		return nextLine != null;
	}

	@Override
	public String next() {
		String result = nextLine;
		readNextLine();
		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	protected void readNextLine() {
		try {
			nextLine = in.readLine();
		} catch (IOException canNotHappen) {
			throw new AssertionError(canNotHappen);
		}
	}
}
