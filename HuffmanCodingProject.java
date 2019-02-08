package dsa.huffmancoding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;


public class HuffmanCoding {

	final static int count[]=new int[256];

	@SuppressWarnings("resource")
	public static void main(String[] args)  {
		String str;
		try {
			str = new Scanner(new File("Dictionary.txt")).useDelimiter("\\A").next();
			System.out.println(str);
			int count[]=frequency(str);
			Node rootNode=buildHuffmanTree(count);
			decode(new EncodedResult(generateBinaryData(str,(buildTable(rootNode))),rootNode));
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		}
	}

	static class Node implements Comparable<Node>{
		private  char character;
		private   int count;
		private   Node leftChild;
		private   Node rightChild;

		//Node class with its attributes
		public  Node(char character,int count, Node leftChild, Node rightChild){
			this.character=character;
			this.count=count;
			this.leftChild=leftChild;
			this.rightChild=rightChild;
		}

		boolean isNodeLeaf(){
			return (this.leftChild==null && this.rightChild==null);
		}

		@Override
		public int compareTo(Node o) {
			// TODO Auto-generated method stub
			return 0;
		}

	}
	static class EncodedResult{
		final Node root;
		final String encodedBinaryData;
		public EncodedResult(String encodeBinaryData,Node root){
			this.root=root;
			this.encodedBinaryData=encodeBinaryData;
		}
		public Node getRoot(){
			return this.root;
		}
		public String getEncodedBinaryData(){
			return this.encodedBinaryData;
		}
	}
	static class charString{
		char character;
		String str;

		public charString (char character,String str){
			this.character=character;
			this.str=str;
		}
		public char getCharacter() {
			return character;
		}
		public String getStr() {
			return str;
		}
	}

		//method to count the frequencies of the input stream
		public static int[] frequency(String str){
			for(int i=0;i<str.length();i++){
				count[str.charAt(i)]++;
			}
			for(int j=0;j<count.length;j++){
				if(count[j]>0)
					System.out.printf("%c  %d times\n", j, count[j]);
			}
			return count;
		}

		//Building the Huffman Tree
		private static Node buildHuffmanTree(final int []count){
			Node [] nArr=new Node[256];
			int j=0;
			for(char i=0;i<256;i++){
				if(count[i]>0)
					nArr[j++]=new Node(i,count[i],null,null);
			}
			while(j>1){
				Node left=findMinNode(nArr,j--);
				Node right=findMinNode(nArr,j--);
				Node parent=new Node('\0',left.count+right.count,left,right);
				insertInNodeArray(nArr,parent,j++);
			}
			return findMinNode(nArr,1);
		}
	//adding parent node to Node array
	private static void insertInNodeArray(Node [] nArr, Node parent,int size) {
			nArr[size]=parent;
		}
	//finding min node from Node array
	private static Node findMinNode(Node[] nArr,int size) {
		int min=nArr[0].count;
		int index=0;
		for(char i=1;i<size;i++){
			if(min>nArr[i].count){
				min=nArr[i].count;
				index=i;
			}else if(min==nArr[i].count){
				if(nArr[i].character < nArr[index].character){
					min=nArr[i].count;
					index=i;
				}
			}
		}
		Node node=nArr[index];
		//Deleting the min node from the node array after extraction
		int z;
		for(z=index;z<=size-2;z++){
			nArr[z]=nArr[z+1];
		}
		nArr[z]=null;
		return node;

		}

			private static charString [] buildTable(Node rootNode){
				charString [] cs=new charString[256];
				buildTableImpl(rootNode,"",cs);
				for(int i=0;i<size;i++){
					if(cs[i].getCharacter()!=0 && cs[i].getStr()!=null)
						System.out.println(cs[i].getCharacter()+" "+cs[i].getStr());
				}
				return cs;
			}
			static int i=0;
			static int size;
			private static void buildTableImpl(Node node, String string,charString [] cs) {
				if(!node.isNodeLeaf()){
					buildTableImpl(node.leftChild,string+'0',cs);
					buildTableImpl(node.rightChild,string+'1',cs);
				}else{
					cs[i]=new charString(node.character,string);
					i++;
				}
				size=i;
			}
			//methods to generate Binary Data
			private static  String generateBinaryData(String str,charString [] cs) {
				final StringBuilder sb=new StringBuilder();
				for(final char character:str.toCharArray()){
					for(int i=0;i<size;i++){
						if(character==cs[i].character){
							sb.append(cs[i].str);
						}
					}
				}

				System.out.print("Encoded result for String is "+sb.toString());
				try {
					PrintWriter output=new PrintWriter(new File("EncodedOutput.txt"));
					output.println("Output    "+sb);
					output.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				return sb.toString();
			}

			public static String decode(final EncodedResult result){
				final StringBuilder decoded=new StringBuilder();
				Node node=result.getRoot();
				for(int i=0;i<result.getEncodedBinaryData().length();){
					while(!node.isNodeLeaf()){
						char bit=result.getEncodedBinaryData().charAt(i);
						if(bit=='1'){
							node=node.rightChild;
						}else if(bit=='0'){
							node=node.leftChild;
						}
						i++;
					}
					decoded.append(node.character);
					node=result.getRoot();
				}
				try {
					PrintWriter output=new PrintWriter(new File("DecodedOutput.txt"));
					output.println("Output  \n"+decoded.toString());
					output.close();
					System.out.println("");
					System.out.println("Decoded  \n"+decoded.toString());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				return decoded.toString();
			}
}
