public class LLSparseVec implements SparseVec 
{
	private int len;				// length of LLVector
	private int nElements = 0;		// number of nonzero elements, set to zero;
	private Node header = null; 	// header sentinel, dummy node
	private Node trailer = null;	// trailer sentinel, dummy node
	// Constructor
	public LLSparseVec(int len)
	{	
		if( len <= 0) len = 1;		// if zero or negative, set length = 1
		this.len = len;
		//Create the Vector
		header = new Node(); 		// creating header
		trailer = new Node(); 		// creating trailer
		header.setNext(trailer); 	// header is followed by trailer
		trailer.setPrev(header);	// trailer is preceded by header
    }
	// check if the given index is out of bounds
	private boolean outOfBounds(int idx)
	{
		return((idx < 0) || (idx >= len));
	}
	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		return len;
	}

	@Override
	public int numElements() {
		// TODO Auto-generated method stub
		return nElements;
	}

	@Override
	public int getElement(int idx) {
		// TODO Auto-generated method stub
		// If index is out of bounds or list empty exit
		if(outOfBounds(idx))
			return Integer.MIN_VALUE;
		Node temp = header.getNext();
		while(temp != trailer)
		{
			if(idx == temp.getIndex())
				return temp.getData();
			// If index is < than temp's index it doesn't exist, exit.
			if(idx < temp.getIndex()) 
				break;
			temp = temp.getNext();	
		}	
		return 0;
	}

	@Override
	public void clearElement(int idx) {
		// TODO Auto-generated method stub
		if(outOfBounds(idx))		// If index out of bounds exit
			return;
		Node temp = header.getNext();
		Node predecessor;
		Node sucessor;
		while(temp != trailer)
		{
			if(idx == temp.getIndex())
			{
				predecessor = temp.getPrev();
				sucessor = temp.getNext();
				predecessor.setNext(sucessor);
				sucessor.setPrev(predecessor);
				nElements--;
			}
			// If index is < than temp's index it doesn't exist, exit.
			if(idx < temp.getIndex())
				break;
			temp = temp.getNext();
		}
	}
	@Override
	public void setElement(int idx, int val) {
		//System.out.println("  idx " + idx + " value " + val);
		// TODO Auto-generated method stub
		if(outOfBounds(idx))
			throw new IllegalStateException("ERROR, index out of bounds.");
		// If the list is empty add between header and trailer.
		else if(numElements() == 0 && val != 0)	
			addBetween(idx, val, header, trailer);
		// If val = 0 remove the index
		else if(val == 0)
			clearElement(idx);
		// In the case that index added is smaller than the first node's index
		else if ( idx < header.getNext().getIndex())
			addBetween(idx, val, header, header.getNext()); 
		// In the case the index added is greater than the last node's index
		else if ( idx > trailer.getPrev().getIndex())
			addBetween(idx, val, trailer.getPrev(), trailer);
		else
		{
			Node temp = header.getNext();
			while(temp != trailer)
			{
				if( idx == temp.getIndex() && val != temp.getData())
				{
					temp.setData(val);
					return;
				}
				else if(idx == temp.getIndex() && val == temp.getData() )
					return;
				if( idx < temp.getIndex())
				{
					addBetween(idx, val, temp.getPrev(), temp);
					return;
				}
			
				temp = temp.getNext();
			}	
		}		
	}
	@Override
	public int[] getAllIndices() {
		// TODO Auto-generated method stub
		int[] idx = new int[nElements];
		Node tempIdx = header.getNext();
		for(int i = 0; tempIdx != trailer; i++)
		{
			idx[i] = tempIdx.getIndex();
			tempIdx = tempIdx.getNext();
		}
		return idx;
	}

	@Override
	public int[] getAllValues() {
		// TODO Auto-generated method stub
		int[] val = new int[nElements];
		Node tempVal = header.getNext();
		for(int i = 0; tempVal != trailer; i++)
		{
			val[i] = tempVal.getData();
			tempVal = tempVal.getNext();
		}
		return val;
		
	}
	
	private void addBetween(int idx, int val, Node predecessor, Node successor)
	{	// create and link a new node
		Node newest = new Node (idx, val, predecessor, successor);
		predecessor.setNext(newest);
		successor.setPrev(newest);
		nElements++;
	}
	@Override
	public SparseVec addition(SparseVec otherV) {
		// TODO Auto-generated method stub
		if(this.len != otherV.getLength())
			return null;					//exit if lengths not even.
		
		SparseVec newVec = new LLSparseVec(len);
		int temp;
		
		
		for(int i = 0; i < len; i++ )
		{
			temp = this.getElement(i) + otherV.getElement(i);
			newVec.setElement(i, temp);
		}
		return newVec;		
	}
	@Override
	public SparseVec subtraction(SparseVec otherV) {
		// TODO Auto-generated method stub
		if(this.len != otherV.getLength())
			return null;				//exit if lengths not even.
		
		SparseVec newVec = new LLSparseVec(len);
		int temp;
		
		for(int i = 0; i < len; i++ )
		{
			temp = this.getElement(i) - otherV.getElement(i);
			newVec.setElement(i, temp);
		}
		return newVec;
	}
	@Override
	public SparseVec multiplication(SparseVec otherV) {
		// TODO Auto-generated method stub
		if(this.len != otherV.getLength())
			return null;			//exit if lengths not even.
		SparseVec newVec = new LLSparseVec(len);
		int temp;
		
		for(int i = 0; i < len; i++ )
		{
			temp = this.getElement(i) * otherV.getElement(i);
			newVec.setElement(i, temp);
		}
		return newVec;
	}
	
	
}
