import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Fat {
	
	ByteBuffer table;
	
	Fat(int size){
		table = ByteBuffer.allocate(size);
		table.order(ByteOrder.LITTLE_ENDIAN);
		table.rewind();
	}
	
	public void addBlock(byte[] block){
		table.put(block);
	}
	
	public int getEntry(int cluster){
		return table.getShort(cluster*2);
	}
	

}