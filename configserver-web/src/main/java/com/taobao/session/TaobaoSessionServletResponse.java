package com.taobao.session;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.lang.time.FastDateFormat;
import org.apache.log4j.Logger;

/**
 * ��Ҫ��Ҫʵ��buffer�Լ�lazyCommit���ܣ�
 * ������WebX��BufferedResponseImpl�Լ�LazyCommitRequestContext
 *
 * @author huangshang, hengyi
 *
 */
public class TaobaoSessionServletResponse extends HttpServletResponseWrapper {

    private static Logger log = Logger.getLogger(TaobaoSessionServletResponse.class);

    private static final TimeZone GMT_TIME_ZONE = TimeZone.getTimeZone("GMT");

    private static final String COOKIE_DATE_PATTERN = "EEE, dd-MMM-yyyy HH:mm:ss 'GMT'";

    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance(COOKIE_DATE_PATTERN, GMT_TIME_ZONE,
            Locale.US);

    private static final String EXPIRES = "Expires";

    private static final String PATH = "Path";

    private static final String DOMAIN = "Domain";

    private static final String HTTP_ONLY = "HttpOnly";

    private static final String SET_COOKIE = "Set-Cookie";

    private static final String COOKIE_SEPARATOR = ";";

    private static final String KEY_VALUE_SEPARATOR = "=";

    private BufferedServletOutputStream stream;
    private BufferedServletWriter writer;
    private PrintWriter streamAdapter;
    private ServletOutputStream writerAdapter;

    private boolean flushed;
    private int status;
    private SendError sendError;
    private String sendRedirect;
    private TaobaoSession session;

    public TaobaoSession getSession() {
		return session;
	}

	public void setSession(TaobaoSession session) {
		this.session = session;
	}

	/**
     * Ĭ�Ϲ��캯��
     *
     * @param response
     */
    public TaobaoSessionServletResponse(HttpServletResponse response) {
        super(response);
        this.flushed = false;
    }

    public void addCookie(TaobaoCookie cookie) {
        if (!cookie.isHttpOnly()) {
            super.addCookie(cookie);
        } else {
            // ��Servlet 3.0��Ͳ���Ҫ��������δ����ˣ�����ֱ��cookie.setHttpOnly(true)
            // Ȼ��response.addCookie(cookie);
            String cookieString = buildHttpOnlyCookieString(cookie);
            addHeader(SET_COOKIE, cookieString);
        }
    }

    private String buildHttpOnlyCookieString(TaobaoCookie cookie) {
        StringBuilder cookieBuilder = new StringBuilder();

        cookieBuilder.append(cookie.getName()).append(KEY_VALUE_SEPARATOR).append(cookie.getValue());
        cookieBuilder.append(COOKIE_SEPARATOR);

        if (cookie.getDomain() != null) {
            cookieBuilder.append(DOMAIN).append(KEY_VALUE_SEPARATOR).append(cookie.getDomain());
            cookieBuilder.append(COOKIE_SEPARATOR);
        }

        if (cookie.getPath() != null) {
            cookieBuilder.append(PATH).append(KEY_VALUE_SEPARATOR).append(cookie.getPath());
            cookieBuilder.append(COOKIE_SEPARATOR);
        }

        if (cookie.getMaxAge() >= 0) {
            cookieBuilder.append(EXPIRES).append(KEY_VALUE_SEPARATOR).append(getCookieExpires(cookie));
            cookieBuilder.append(COOKIE_SEPARATOR);
        }

        cookieBuilder.append(HTTP_ONLY);

        return cookieBuilder.toString();
    }

    private String getCookieExpires(TaobaoCookie cookie) {
        String result = null;

        int maxAge = cookie.getMaxAge();
        if (maxAge > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, maxAge);
            result = DATE_FORMAT.format(calendar);
        } else { // maxAge == 0
            result = DATE_FORMAT.format(0); // maxAgeΪ0ʱ��ʾ��Ҫɾ���cookie����˽�ʱ����Ϊ��Сʱ�䣬��1970��1��1��
        }

        return result;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (stream != null) {
            return stream;
        }

        if (writer != null) {
            // ���getWriter�����Ѿ������ã���writerת����OutputStream
            // ����������������������ڴ濪����׼��servlet engine���������������Σ�
            // ֻ������servlet engine��Ҫ����������resin����
            if (writerAdapter != null) {
                return writerAdapter;
            } else {
                log.warn("Attampt to getOutputStream after calling getWriter.  This may cause unnecessary system cost.");
                writerAdapter = new WriterOutputStream(writer, getCharacterEncoding());
                return writerAdapter;
            }
        }
        if (this.isWriterBuffered) {
        	ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            stream = new BufferedServletOutputStream(bytes);

            log.debug("Created new byte buffer");

            return stream;
		} else {
			this.getSession().commit();
			return super.getOutputStream();
		}
        
    }

    @Override
    public PrintWriter getWriter() throws IOException {
		if (writer != null) {
			return writer;
		}

		if (stream != null) {
			// ���getOutputStream�����Ѿ������ã���streamת����PrintWriter��
			// ����������������������ڴ濪����׼��servlet engine���������������Σ�
			// ֻ������servlet engine��Ҫ����������resin����
			if (streamAdapter != null) {
				return streamAdapter;
			} else {
				log.warn("Attampt to getWriter after calling getOutputStream.  This may cause unnecessary system cost.");
				streamAdapter = new PrintWriter(new OutputStreamWriter(stream,getCharacterEncoding()), true);
				return streamAdapter;
			}
		}
		if (this.isWriterBuffered) {
			StringWriter chars = new StringWriter();

			writer = new BufferedServletWriter(chars);

			log.debug("Created new character buffer");

			return writer;
		} else {
			this.getSession().commit();
			return super.getWriter();
		}
	}

    /**
     * ����content���ȡ���Ч��
     *
     * @param length content����
     */
    @Override
    public void setContentLength(int length) {
    	if(!isWriterBuffered)
          super.setContentLength(length);
    }

    @Override
    public void flushBuffer() throws IOException {
        flushBufferAdapter();

        if (writer != null) {
            writer.flush();
        } else {
            stream.flush();
        }

        this.flushed = true;
    }

    @Override
    public void sendError(int status) throws IOException {
        sendError(status, null);
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public void sendError(int status, String message) throws IOException {
        if ((sendError == null) && (sendRedirect == null)) {
            sendError = new SendError(status, message);
        }
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        if ((sendError == null) && (sendRedirect == null)) {
            sendRedirect = location;
        }
    }

    @Override
    public void setHeader(String key, String value) {
        super.setHeader(key, value);
    }

    @Override
    public void resetBuffer() {
        flushBufferAdapter();

        if (stream != null) {
            ((BufferedServletOutputStream) stream).updateOutputStream(new ByteArrayOutputStream());
        }

        if (writer != null) {
            ((BufferedServletWriter) writer).updateWriter(new StringWriter());
        }

        super.resetBuffer();
    }

    /**
     * ��buffer�е������ύ�������servlet������С�
     *
     * <p>
     * ������û��ִ�й�<code>getOutputStream</code>��<code>getWriter</code>
     * ��������÷��������κ����顣
     * </p>
     *
     * @throws IOException ����������ʧ��
     * @throws IllegalStateException �������bufferģʽ����bufferջ�в�ֹһ��buffer
     */
    public void commitBuffer() throws IOException {
        if (status > 0) {
            log.debug("Set HTTP status to " + status);
            super.setStatus(status);
        }

        if (sendError != null) {
            if (sendError.message == null) {
                log.debug("Set error page: " + sendError.status);
                super.sendError(sendError.status);
            } else {
                log.debug("Set error page: " + sendError.status + " " + sendError.message);
                super.sendError(sendError.status, sendError.message);
            }
        } else if (sendRedirect != null) {
            log.debug("Set redirect location to " + sendRedirect);

            // ��location���������ת��һ�£��������ȷ�����US_ASCII�ַ��URL��ȷ���
       //  String charset = getCharacterEncoding();
//
//            if (charset != null) {
//                sendRedirect = new String(sendRedirect.getBytes(charset), "8859_1");
//            }

            super.sendRedirect(sendRedirect);
        } else if (stream != null) {
            flushBufferAdapter();

            OutputStream ostream = super.getOutputStream();
            ByteArray bytes = this.stream.getBytes().toByteArray();

            bytes.writeTo(ostream);

            log.debug("Committed buffered bytes to the Servlet output stream");
        } else if (writer != null) {
            flushBufferAdapter();

            PrintWriter writer = super.getWriter();
       
           
			try { 
				String chars ;
				chars = this.writer.getChars().toString();
				  writer.write(chars);
			} catch (NullPointerException e) {
				log.debug(e+"write has been closed");
			}
            
          

            log.debug("Committed buffered characters to the Servlet writer");
        }

        if (this.flushed) {
            super.flushBuffer();
        }
    }

    /**
     * ��ϴbuffer adapter��ȷ��adapter�е���Ϣ��д��buffer�С�
     */
    private void flushBufferAdapter() {
        if (streamAdapter != null) {
            streamAdapter.flush();
        }

        if (writerAdapter != null) {
            try {
                writerAdapter.flush();
            } catch (IOException e) {
            }
        }
    }

    /**
     * ���һ�������ݱ������ڴ��е�<code>ServletOutputStream</code>��
     */
    private static class BufferedServletOutputStream extends ServletOutputStream {
        private ByteArrayOutputStream bytes;

        public BufferedServletOutputStream(ByteArrayOutputStream bytes) {
            this.bytes = bytes;
        }

        public void updateOutputStream(ByteArrayOutputStream bytes) {
            this.bytes = bytes;
        }

        public ByteArrayOutputStream getBytes() {
            return this.bytes;
        }

        @Override
        public void write(int b) throws IOException {
            bytes.write((byte) b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            write(b, 0, b.length);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            bytes.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            bytes.flush();
        }

        @Override
        public void close() throws IOException {
            bytes.flush();
            bytes.close();
        }
    }

    /**
     * ���һ�������ݱ������ڴ��е�<code>PrintWriter</code>��
     */
    private static class BufferedServletWriter extends PrintWriter {
        public BufferedServletWriter(StringWriter chars) {
            super(chars);
        }

        public Writer getChars() {
            return this.out;
        }

        public void updateWriter(StringWriter chars) {
            this.out = chars;
        }
        public void close() {
        	   try {
				this.out.close();
			} catch (IOException e) {
			}
        	   
            }
    }

    /**
     * ��<code>Writer</code>���䵽<code>ServletOutputStream</code>��
     */
    private static class WriterOutputStream extends ServletOutputStream {
        private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        private Writer writer;
        private String charset;

        public WriterOutputStream(Writer writer, String charset) {
            this.writer = writer;
            this.charset = (null == charset ? "ISO-8859-1" : charset);
        }

        @Override
        public void write(int b) throws IOException {
            buffer.write((byte) b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            buffer.write(b, 0, b.length);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            buffer.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            ByteArray bytes = buffer.toByteArray();

            if (bytes.getLength() > 0) {
                ByteArrayInputStream inputBytes = new ByteArrayInputStream(bytes.getBytes(), bytes.getOffset(), bytes
                        .getLength());
                InputStreamReader reader = new InputStreamReader(inputBytes, charset);

                io(reader, writer);
                writer.flush();

                buffer.reset();
            }
        }

        @Override
        public void close() throws IOException {
            this.flush();
        }

        private void io(Reader in, Writer out) throws IOException {
            char[] buffer = new char[8192];
            int amount;

            while ((amount = in.read(buffer)) >= 0) {
                out.write(buffer, 0, amount);
            }
        }
    }

    /**
     * ���һ��byte���顣
     *
     * @author Michael Zhou
     * @version $Id: ByteArray.java 593 2004-02-26 13:47:19Z baobao $
     */
    private static class ByteArray {
        private byte[] bytes;
        private int offset;
        private int length;

        public ByteArray(byte[] bytes, int offset, int length) {
            this.bytes = bytes;
            this.offset = offset;
            this.length = length;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public int getOffset() {
            return offset;
        }

        public int getLength() {
            return length;
        }

        public void writeTo(OutputStream out) throws IOException {
            out.write(bytes, offset, length);
        }
    }

    /**
     * ��ͬ����<code>ByteArrayOutputStream</code>�滻����, ִ��<code>toByteArray()</code>
     * ����ʱ���ص���ֻ�����ڲ��ֽ�����, ������û�б�Ҫ���ֽڸ���. ��������ֲ��IBM developer works��������,
     * �μ�package�ĵ�.
     *
     * @author Michael Zhou
     * @version $Id: ByteArrayOutputStream.java 593 2004-02-26 13:47:19Z baobao
     *          $
     */
    private static class ByteArrayOutputStream extends OutputStream {
        private static final int DEFAULT_INITIAL_BUFFER_SIZE = 8192;

        // internal buffer
        private byte[] buffer;
        private int index;
        private int capacity;

        // is the stream closed?
        private boolean closed;

        // is the buffer shared?
        private boolean shared;

        public ByteArrayOutputStream() {
            this(DEFAULT_INITIAL_BUFFER_SIZE);
        }

        public ByteArrayOutputStream(int initialBufferSize) {
            capacity = initialBufferSize;
            buffer = new byte[capacity];
        }

        @Override
        public void write(int datum) throws IOException {
            if (closed) {
                throw new IOException("Stream closed");
            } else {
                if (index >= capacity) {
                    // expand the internal buffer
                    capacity = (capacity * 2) + 1;

                    byte[] tmp = new byte[capacity];

                    System.arraycopy(buffer, 0, tmp, 0, index);
                    buffer = tmp;

                    // the new buffer is not shared
                    shared = false;
                }

                // store the byte
                buffer[index++] = (byte) datum;
            }
        }

        @Override
        public void write(byte[] data, int offset, int length) throws IOException {
            if (data == null) {
                throw new NullPointerException();
            } else if ((offset < 0) || ((offset + length) > data.length) || (length < 0)) {
                throw new IndexOutOfBoundsException();
            } else if (closed) {
                throw new IOException("Stream closed");
            } else {
                if ((index + length) > capacity) {
                    // expand the internal buffer
                    capacity = (capacity * 2) + length;

                    byte[] tmp = new byte[capacity];

                    System.arraycopy(buffer, 0, tmp, 0, index);
                    buffer = tmp;

                    // the new buffer is not shared
                    shared = false;
                }

                // copy in the subarray
                System.arraycopy(data, offset, buffer, index, length);
                index += length;
            }
        }

        @Override
        public void close() {
            closed = true;
        }

        public ByteArray toByteArray() {
            shared = true;
            return new ByteArray(buffer, 0, index);
        }

        public void reset() throws IOException {
            if (closed) {
                throw new IOException("Stream closed");
            } else {
                if (shared) {
                    // create a new buffer if it is shared
                    buffer = new byte[capacity];
                    shared = false;
                }

                // reset index
                index = 0;
            }
        }
    }

    /**
     * ��ͬ����<code>ByteArrayInputStream</code>�滻����, ��������ֲ��IBM developer works��������,
     * �μ�package�ĵ�.
     *
     * @author Michael Zhou
     * @version $Id: ByteArrayInputStream.java 509 2004-02-16 05:42:07Z baobao $
     */
    private static class ByteArrayInputStream extends InputStream {
        // buffer from which to read
        private byte[] buffer;
        private int index;
        private int limit;
        private int mark;

        // is the stream closed?
        private boolean closed;

        public ByteArrayInputStream(byte[] data, int offset, int length) {
            if (data == null) {
                throw new NullPointerException();
            } else if ((offset < 0) || ((offset + length) > data.length) || (length < 0)) {
                throw new IndexOutOfBoundsException();
            } else {
                buffer = data;
                index = offset;
                limit = offset + length;
                mark = offset;
            }
        }

        @Override
        public int read() throws IOException {
            if (closed) {
                throw new IOException("Stream closed");
            } else if (index >= limit) {
                return -1; // EOF
            } else {
                return buffer[index++] & 0xff;
            }
        }

        @Override
        public int read(byte[] data, int offset, int length) throws IOException {
            if (data == null) {
                throw new NullPointerException();
            } else if ((offset < 0) || ((offset + length) > data.length) || (length < 0)) {
                throw new IndexOutOfBoundsException();
            } else if (closed) {
                throw new IOException("Stream closed");
            } else if (index >= limit) {
                return -1; // EOF
            } else {
                // restrict length to available data
                if (length > (limit - index)) {
                    length = limit - index;
                }

                // copy out the subarray
                System.arraycopy(buffer, index, data, offset, length);
                index += length;
                return length;
            }
        }

        @Override
        public long skip(long amount) throws IOException {
            if (closed) {
                throw new IOException("Stream closed");
            } else if (amount <= 0) {
                return 0;
            } else {
                // restrict amount to available data
                if (amount > (limit - index)) {
                    amount = limit - index;
                }

                index += (int) amount;
                return amount;
            }
        }

        @Override
        public int available() throws IOException {
            if (closed) {
                throw new IOException("Stream closed");
            } else {
                return limit - index;
            }
        }

        @Override
        public void close() {
            closed = true;
        }

        @Override
        public void mark(int readLimit) {
            mark = index;
        }

        @Override
        public void reset() throws IOException {
            if (closed) {
                throw new IOException("Stream closed");
            } else {
                // reset index
                index = mark;
            }
        }

        @Override
        public boolean markSupported() {
            return true;
        }
    }
    private boolean isWriterBuffered = true;
    
    public boolean isWriterBuffered() {
		return isWriterBuffered;
	}
    
    /**
     * ����isWriterBufferedģʽ��������ó�<code>true</code>����ʾ��������Ϣ�������ڴ��У�����ֱ�������ԭʼresponse�С�
     * 
     * <p>
     * �˷���������<code>getOutputStream</code>��<code>getWriter</code>����֮ǰִ�У������׳�<code>IllegalStateException</code>��
     * </p>
     *
     * @param buffering �Ƿ�buffer����
     *
     * @throws IllegalStateException <code>getOutputStream</code>��<code>getWriter</code>�����Ѿ���ִ��
     */
public void setWriterBuffered(boolean isWriterBuffered) {
	if ((stream == null) && (writer == null)) {
        if (this.isWriterBuffered != isWriterBuffered) {
            this.isWriterBuffered = isWriterBuffered;
            log.debug("Set WriterBuffered " + (isWriterBuffered ? "on": "off"));
        }
    } else {
        if (this.isWriterBuffered != isWriterBuffered) {
            throw new IllegalStateException("Unable to change the isWriterBuffered mode since the getOutputStream() or getWriter() method has been called");
        }
    }
}

	/**
     * ����sendError����Ϣ��
     */
    private static class SendError {
        public final int status;
        public final String message;

        public SendError(int status, String message) {
            this.status = status;
            this.message = message;
        }
    }

}
