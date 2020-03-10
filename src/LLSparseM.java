
public class LLSparseM implements SparseM 
{	
	private int nrows, ncols, size;
	private int nElements = 0; 	// number of nonzero elements, initialized to be zero
	private Node header;
	private Node trailer;
	// Constructor
    public LLSparseM(int nr, int nc){
    	if(nr <= 0) nr = 1;	// if zero or negative nr, set nr = 1;
		if(nc <= 0) nc = 1;	// if zero or negative nc, set nc = 1;	
		nrows = nr;	
		ncols = nc;
		header = new Node(); 		// creating header
		trailer = new Node(); 		// creating trailer
		header.setNextRowHead(trailer); 	// header is followed by trailer
		trailer.setPrevRowHead(header);	// trailer is preceded by header
    }
    // check if the given (ridx, cidx) is out of bounds
 	private boolean outOfBounds(int ridx, int cidx){
 		return((ridx < 0) || (ridx >= nrows) || (cidx < 0) || (cidx >= ncols));
 	}
 	// Returns the total of row headers
 	public int getSize()
 	{
 		return size;
 	}
	@Override
	public int nrows() {
		// TODO Auto-generated method stub
		return nrows;
		/*
		int nr = 0;
		Node tempR = header.getNextRowHead();
		while(tempR != trailer)
		{
			tempR = tempR.getNextRowHead();
			nr++;
			System.out.println("nr " + nr);
		}
		return nr;	*/
	}

	@Override
	public int ncols() {
		// TODO Auto-generated method stub
		
		return ncols;
		/*int nc = 0;
		Node tempR = header.getNextRowHead();
		while(tempR != trailer)
		{
			Node currCol = tempR.getNext();
			while(currCol != null)
			{
				currCol = currCol.getNext();
				nc++;
			}
			tempR = tempR.getNextRowHead();
		}
		return nc;*/
	}

	@Override
	public int numElements() {
		// TODO Auto-generated method stub
		return nElements;
	}

	@Override
	public int getElement(int ridx, int cidx) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clearElement(int ridx, int cidx) {
		// TODO Auto-generated method stub
		// if empty return
		if(header == trailer )
			return;
		Node tempR = header.getNextRowHead();
		while (tempR != trailer)
		{
			// find the row and and call clearCol method
			if(ridx == tempR.getRowHeadIdx())
			{
				clearCol(tempR, cidx);
				break;
			}
			// The row header idx is smaller than the temp row head, it means it doesn't exist.
			else if( ridx < tempR.getNextRowHead().getRowHeadIdx())
				break;	
			tempR = tempR.getNextRowHead();
		}
	}

	@Override
	public void setElement(int ridx, int cidx, int val) {
		// TODO Auto-generated method stub
		if(outOfBounds(ridx,cidx))
			return;
		else if(val == 0)
			clearElement(ridx,cidx);
		// In the case that there is no next row, matrix is empty
		else if ( header.getNextRowHead() == trailer && val != 0)
			addBetweenRow(ridx, cidx, val, header, trailer);
		// In the case that the row header is smaller than the first row header.
		else if( ridx < header.getNextRowHead().getRowHeadIdx() && val != 0)
			addBetweenRow(ridx, cidx, val, header, header.getNextRowHead());
		// In the case that row header is greater than the last row header in the list.
		else if( ridx > trailer.getPrevRowHead().getRowHeadIdx() )
		{
			//System.out.println("ridx " + ridx + " trailer rowhead idx " + trailer.getPrevRowHead().getRowHeadIdx() );

			addBetweenRow(ridx, cidx, val, trailer.getPrevRowHead(), trailer);
		}
		else
		{
			Node tempR = header.getNextRowHead();
			while( tempR != trailer)
			{
				if( ridx == tempR.getRowHeadIdx())
				{
					setCol(tempR, cidx, val);
					break;
				}
				else if(ridx < tempR.getRowHeadIdx())
				{
					addBetweenRow(ridx, cidx, val, tempR.getPrevRowHead(), tempR);
					break;
				}
				else 
					tempR = tempR.getNextRowHead();
			}
		}
	}
	// Adds a row header.
	public void addBetweenRow(int ridx, int cidx, int val, Node prevRowHead, Node nextRowHead)
	{	
		
		Node newestRowHead = new Node(ridx, prevRowHead, nextRowHead, null );
		prevRowHead.setNextRowHead(newestRowHead);
		nextRowHead.setPrevRowHead(newestRowHead);
		newestRowHead.setNext(new Node(cidx, val, newestRowHead, null));
		newestRowHead.addSize();
		nElements++;
		size++;
		
	}
	// Sets the col for the row header called upon
	public void setCol(Node currRow, int cidx, int val)
	{	
		// Gives us the first column that is pointed by that current Row
		Node currCol = currRow.getNext();
		while(currCol != null)
		{
			if(cidx < currCol.getIndex())
			{
				addBetweenCols(cidx, val, currCol.getPrev(), currCol);
				currRow.addSize();
				break;
			}
			else if(cidx == currCol.getIndex() && val != currCol.getData())	
			{
				currCol.setData(val);
				break;
			}
			else if(cidx == currCol.getIndex() && val == currCol.getData())	
				break;
			else if( currCol.getNext() == null )
			{
				addBetweenCols(cidx, val, currCol, null);
				currRow.addSize();
			}
			currCol = currCol.getNext();
		}
			
	}
	public void addBetweenCols(int cidx, int val, Node predecessor, Node successor)
	{
		// Gives us the first column that is pointed by that current Row
		
		Node newCol = new Node(cidx, val, predecessor, successor);
		predecessor.setNext(newCol);
		if( successor != null)
			successor.setPrev(newCol);
		nElements++;	
					
	}
	// This method clears a column in the 
	public void clearCol(Node currRow, int cidx )
	{
		Node currCol = currRow.getNext();
		Node predecessor;
		Node successor;
		
		while(currCol != null)
		{
			if( cidx == currCol.getIndex())
			{	
				currRow.minusSize();
				nElements--;
				// find predecessor and successor nodes
				predecessor = currCol.getPrev();
				successor = currCol.getNext();
				// if the curr col removed is the first one 
				if(predecessor == currRow)
				{
					/* if the colum being deleted is the only one,
					clear the row header also */
					if( successor == null)
						clearRowHead(currRow);
					else 
						currRow.setNext(successor);
				}
				// disconect the current col
				else 
				{
					predecessor.setNext(successor);
					if(successor != null)
						successor.setPrev(predecessor);
				}
				break;
			}
			// if column is less that current column it doesn't exist
			else if( cidx < currCol.getIndex())
				break;
			currCol = currCol.getNext();
		}
	}
	// This method will clear the row header
	public void clearRowHead(Node currRow)
	{
		Node prevRowHead = currRow.getPrevRowHead();
		Node nextRowHead = currRow.getNextRowHead();
		// unlink current row head
		prevRowHead.setNextRowHead(nextRowHead);
		nextRowHead.setPrevRowHead(prevRowHead);
		size--;
	}
	@Override
	public int[] getRowIndices() {
		// TODO Auto-generated method stub
		Node tempR = header.getNextRowHead();
		int[] ridx = new int[size];
		for(int i = 0; i < size; i++)
		{
			ridx[i] = tempR.getRowHeadIdx();
			tempR = tempR.getNextRowHead();
		}
		return ridx;
	}

	@Override
	public int[] getOneRowColIndices(int ridx) {
		// TODO Auto-generated method stub
		Node tempR = header.getNextRowHead();
		Node currCol;
		int[] cidx;
		while(tempR != trailer)
		{
			if(ridx == tempR.getRowHeadIdx())
				break;
			tempR = tempR.getNextRowHead();
		}
		cidx = new int[tempR.getRowHeadSize()];
		currCol = tempR.getNext();
		for(int i = 0; currCol != null; i++)
		{
			cidx[i] = currCol.getIndex();
			currCol = currCol.getNext();
		}
		return cidx;
	}

	@Override
	public int[] getOneRowValues(int ridx) {
		// TODO Auto-generated method stub
		Node tempR = header.getNextRowHead();
		Node currCol;
		
		while(tempR != trailer)
		{
			if( ridx == tempR.getRowHeadIdx())
				break;
			tempR = tempR.getNextRowHead();				
		}
		
		int[] values = new int[tempR.getRowHeadSize()];
		currCol = tempR.getNext();
		
		for(int i = 0; currCol != null; i++)
		{
			values[i] = currCol.getData();
			currCol = currCol.getNext();
		}
		return values;
	}

	@Override
	public SparseM addition(SparseM otherM) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SparseM subtraction(SparseM otherM) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SparseM multiplication(SparseM otherM) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}//  End of LLSparseM Class
