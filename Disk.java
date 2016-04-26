import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Disk {
	
	private String diskName;
	
	public Disk(String _diskName){
		diskName = _diskName;
	}
	
	byte [] getSector(int sectorNum){
		byte array[] = readBlock(diskName, sectorNum);
		return array;
	}
	
	private static byte[] readBlock(String inputFileName, int blockNumber) {
		try {
			return read(inputFileName,  blockNumber * 512, (blockNumber*512)+512);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//
	// found here here
	//	http://c2.com/cgi/wiki?HexDumpInManyProgrammingLanguages
	//
	private static byte[] read(String inputFileName, int start, int end)
			throws FileNotFoundException, IOException {
		File theFile = new File(inputFileName);
		FileInputStream input = new FileInputStream(theFile);
		int skipped = 0;
		while (skipped < start) {
			skipped += input.skip(start - skipped);
		}
		int length = (int) (Math.min(end, theFile.length()) - start);
		byte[] bytes = new byte[length];
		int bytesRead = 0;
		while (bytesRead < bytes.length) {
			bytesRead = input.read(bytes, bytesRead, bytes.length - bytesRead);
			if (bytesRead == -1) {
				break;
			}
		}
		input.close();
		return bytes;
	}

}
