package jsimple.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import jsimple.util.ByteArrayRange;
import jsimple.util.StringUtils;

import org.junit.Test;

/**
 * @author Dorin Suletea
 * @since 12/1/2014
 */
public class ByteArrayOutputStreamTest {

	@Test
	public void testWriteReadBytes() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		baos.closeAndGetByteArray()
//		baos.getLength()
		byte[] data = new byte[3];
		data[0] = 1;
		data[1] = 2;
		data[2] = 3;
		baos.write(data);

		byte[] sut = baos.getByteArray().toByteArray();

		assertArrayEquals(data, sut);
		baos.close();
	}

	@Test
	public void testReset() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] data = new byte[3];
		byte newFirst = 5;
		data[0] = 1;
		data[1] = 2;
		data[2] = 3;
		baos.write(data);
		// reset the stream , replace first byte with 5
		baos.reset();
		baos.write(newFirst);

		byte[] sut = baos.getByteArray().toByteArray();
		baos.close();
		
		assertEquals(sut[0], newFirst);
		assertEquals(sut[1], data[1]);
		assertEquals(sut[2], data[2]);

	}

	// the stream is closed multiple times , the trigger should be activated
	// only once
	private int closedListenerTriggeredCount = 0;

	@Test
	public void testCloseListenerTriggerCount() {
		closedListenerTriggeredCount = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.setClosedListener(new ClosedListener() {
			@Override
			public void onClosed() {
				closedListenerTriggeredCount++;
			}
		});

		baos.close();
		baos.close();
		assertEquals(1, closedListenerTriggeredCount);
	}

	@Test
	public void testWriteLatin() {
		String text = "test text";
		ByteArrayOutputStream sut = new ByteArrayOutputStream();
		sut.writeLatin1EncodedString(text);

		String output = StringUtils.toStringFromLatin1Bytes(sut.getByteArray().toByteArray());
		sut.close();

		assertEquals(text, output);
	}

	@Test
	public void testWriteUTF8() {
		String text = "test text UTF8";
		ByteArrayOutputStream sut = new ByteArrayOutputStream();
		sut.writeUtf8EncodedString(text);

		String output = new String(sut.getByteArray().toByteArray());
		sut.close();

		assertEquals(text, output);
	}
	
	@Test
	public void testCloseAndGetByteArray(){
		closedListenerTriggeredCount = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] data = new byte[3];
		data[0] = 1;
		data[1] = 2;
		data[2] = 3;
		baos.write(data);
		
		baos.setClosedListener(new ClosedListener() {
			@Override
			public void onClosed() {
				closedListenerTriggeredCount++;
			}
		});
		ByteArrayRange byteRange = baos.closeAndGetByteArray();
		byte [] bytes = byteRange.toByteArray();
		baos.close();
		
		assertEquals(bytes[0], data[0]);
		assertEquals(bytes[1], data[1]);
		assertEquals(1, closedListenerTriggeredCount);
	}
}
