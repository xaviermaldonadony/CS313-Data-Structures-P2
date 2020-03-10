/*------ This Node class is to be used in a doubly linked list ---*/
public class Node 
{	
		private int data;
		private int index;
		private int rowHeadIdx;
		private int rowSize; // will tell how many cols are attached to it
		private Node prev; // reference to the previous node in the list
		private Node next; // reference to the succeeding node in the list
		private Node prevRowHead;
		private Node nextRowHead;
		private Node colNode;
		// Default constructor
		public Node() {};
		// Constructor that sets up a prev and a next.
		public Node(int idx,int d, Node p, Node n) 
		{
			index = idx;
			data = d;
			prev = p;
			next = n;
		}
		
		// Constructor for a row head node
		public Node(int rHidx, Node pRH, Node nRH, Node toColNode)
		{
			rowHeadIdx = rHidx;
			prevRowHead = pRH;
			nextRowHead = nRH;
			colNode = toColNode;
			rowSize = 0;
		}
		// Getter methods
		public int getData() { return data; }
		public int getIndex() { return index; }
		public int getRowHeadIdx() { return rowHeadIdx; }
		public Node getPrev() { return prev; }
		public Node getNext() { return next; }
		public Node getPrevRowHead() {return prevRowHead;}
		public Node getNextRowHead() { return nextRowHead;}
		public int getRowHeadSize() { return rowSize; }
		// Setter methods
		public void setData(int d) { data = d; }
		public void setRowHeadIdx(int rHidx) { rowHeadIdx = rHidx; }
		public void setPrev(Node p) { prev = p;	}
		public void setNext(Node n) { next = n;	}
		public void setPrevRowHead(Node pRH) { prevRowHead = pRH; }
		public void setNextRowHead(Node nRH) { nextRowHead = nRH; }
		public void addSize() { rowSize++; }
		public void minusSize() { rowSize--; }
		
		
		
		
		
}// ---------------- End Node class ----------

