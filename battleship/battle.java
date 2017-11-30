import java.util.*;

public class battle
{
	private static int[][] ply1board = new int[10][10];
	private static int[][] ply2board = new int[10][10];
	private static boolean[][] played1 = new boolean[10][10];
	private static boolean[][] played2 = new boolean[10][10];
	public static void main(String argv[])
	{
		Scanner in = new Scanner(System.in);
		int hitply1 = 0;
		int hitply2 = 0;
		ai ply2 = new ai();
		ply2.initialise();
		ply2board = randomboard();

		System.out.println("\n\n\nWELCOME TO BATTLESHIP!!!\n\n\n");
		System.out.println("Conventions during the game ->");
		System.out.println("0 -> Empty space or Unknown space.");
		System.out.println("* -> Deployed Ship.");
		System.out.println("X -> Hit Ship.");
		System.out.println("- -> Empty hit.");
		System.out.println("\nLet's Begin!!!\n");
		System.out.println("Deploy your ships ->\n");

		System.out.println("Enter <rand> to deploy your ships at random. Enter anything else to deploy ships on your own.");
		String t = in.next();
		if(t.equals("rand"))
		{
			ply1board = randomboard();
		}
		else
		{
			printdeploy(ply1board);
			ply1board = placeship(ply1board, 5, in);
			printdeploy(ply1board);
			ply1board = placeship(ply1board, 4, in);
			printdeploy(ply1board);
			ply1board = placeship(ply1board, 3, in);
			printdeploy(ply1board);
			ply1board = placeship(ply1board, 3, in);
			printdeploy(ply1board);
			ply1board = placeship(ply1board, 2, in);
			printdeploy(ply1board);	
		}

		while(hitply1<17 && hitply2<17)
		{
			printgame(ply1board, ply2board, played1, played2, hitply1, hitply2);
			int mov1 = getmove(in, played1);
			played1[mov1/10][mov1%10] = true;
			while(ply2board[mov1/10][mov1%10]==1)
			{
				hitply1++;
				printgame(ply1board, ply2board, played1, played2, hitply1, hitply2);
				mov1 = getmove(in, played1);
				played1[mov1/10][mov1%10] = true;
			}

			if(hitply1>=17)
			{
				System.out.println("Player wins");
				return;
			}

			int mov2 = ply2.decmove();
			played2[mov2/10][mov2%10] = true;
			while(ply1board[mov2/10][mov2%10]==1)
			{
				ply2.move(mov2/10, mov2%10, true);
				hitply2++;
				mov2 = ply2.decmove();
				played2[mov2/10][mov2%10] = true;
			}
			ply2.move(mov2/10, mov2%10, false);
		}

		System.out.println("AI wins");
	}
	public static int getmove(Scanner in, boolean[][] arr)
	{
		System.out.println("Enter your move:");
		int r, c;
		while(true)
		{
			r = in.nextInt() - 1;
			c = in.nextInt() - 1;
			if(r>9 || r<0 || c>9 || c<0 || arr[r][c])
			{
				System.out.println("Invalid move. Please Enter again:");
			}
			else
			{
				break;
			}
		}
		return (10*r + c);
	}
	public static int[][] placeship(int[][] arr, int len, Scanner in)
	{
		int[][] ans;
		System.out.println("Is the " + len + "-block long ship going to be <hor> or <ver> ?");
		boolean flag = getdim(in);
		System.out.println("Enter the start coordinates of the " + len + "-block long ship:");
		ans = getcoord(arr, flag, len, in);
		return ans;
	}
	public static int[][] getcoord(int[][] arr, boolean flag, int len, Scanner in)
	{
		int r, c;
		if(!flag)
		{
			while(true)
			{
				r = in.nextInt() - 1;
				c = in.nextInt() - 1;
				if(r>10-len || r<0 || c>9 || c<0)
				{
					System.out.println("Cannot fit the ship. Please enter the coordinates again");
				}
				else
				{
					boolean fl = true;
					for(int i=0;i<len;i++)
					{
						if(!surround(arr, r+i, c))
						{
							fl = false;
						}
					}
					if(fl)
					{
						break;
					}
					else
					{
						System.out.println("Cannot fit the ship. Please enter the coordinates again");
					}
				}
			}
		}
		else
		{
			while(true)
			{
				r = in.nextInt() - 1;
				c = in.nextInt() - 1;
				if(r>9 || r<0 || c>10-len || c<0)
				{
					System.out.println("Cannot fit the ship. Please enter the coordinates again");
				}
				else
				{
					boolean fl = true;
					for(int i=0;i<len;i++)
					{
						if(!surround(arr, r, c+i))
						{
							fl = false;
						}
					}
					if(fl)
					{
						break;
					}
					else
					{
						System.out.println("Cannot fit the ship. Please enter the coordinates again");
					}
				}
			}	
		}
		for(int i=0;i<len;i++)
		{
			if(!flag)
			{
				arr[r + i][c] = 1;
			}
			else
			{
				arr[r][c + i] = 1;	
			}
		}
		return arr;
	}
	public static boolean getdim(Scanner in)
	{
		String dim;
		boolean flag;
		while(true)
		{
			dim = in.next();
			if(dim.equals("hor"))
			{
				flag = true;
				break;
			}
			else if(dim.equals("ver"))
			{
				flag = false;
				break;
			}
			else{
				System.out.println("Did not understand you. Please enter again. \n");
			}
		}
		return flag;
	} 
	public static boolean surround(int[][] arr, int r, int c)
	{
		int r1, c1, c_1, r_1;
		r1 = r+1;
		c1 = c+1;
		r_1 = r-1;
		c_1 = c-1;
		if(r==9)
		{
			r1=9;
		}
		if(r==0)
		{
			r_1=0;
		}
		if(c==9)
		{
			c1=9;
		}
		if(c==0)
		{
			c_1=0;
		}

		if(arr[r][c]==0 && arr[r1][c]==0 && arr[r][c1]==0 && arr[r_1][c]==0 && arr[r][c_1]==0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public static int[][] makeships(int[][] arr, int len, boolean hor)
	{
		Random ab = new Random();
		boolean flag = true;
		while(flag)
		{
			if(hor)
			{
				int r = ab.nextInt(11-len);
				int c = ab.nextInt(10);
				int count = 0;
				for(int i=0;i<len;i++)
				{
					if(surround(arr, r+i, c))
					{
						count++;
					}
				}
				if(count==len)
				{
					for(int i=0;i<len;i++)
					{
						arr[r+i][c] = 1;
					}
					flag = false;
				}
			}
			else
			{
				int r = ab.nextInt(10);
				int c = ab.nextInt(11-len);
				int count = 0;
				for(int i=0;i<len;i++)
				{
					if(surround(arr, r, c+i))
					{
						count++;
					}
				}
				if(count==len)
				{
					for(int i=0;i<len;i++)
					{
						arr[r][c+i] = 1;
					}
					flag = false;
				}
			}
		}
		return arr;
	}
	public static int[][] randomboard()
	{
		int[][] ans = new int[10][10];
		ans = makeships(ans, 5, true);
		ans = makeships(ans, 4, false);
		ans = makeships(ans, 3, true);
		ans = makeships(ans, 3, false);
		ans = makeships(ans, 2, false);
		return ans;
	}
	public static void printgame(int[][] arr1, int[][] arr2, boolean[][] arr3, boolean[][] arr4, int sc1, int sc2)
	{
		System.out.println("Player Board 						AI Board");
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				if(!arr4[i][j])
				{
					if(arr1[i][j]==0)
					{
						System.out.print("0  ");
					}
					else
					{
						System.out.print("*  ");
					}
				}
				else
				{
					if(arr1[i][j]==0)
					{
						System.out.print("-  ");
					}
					else
					{
						System.out.print("X  ");
					}
				}
			}
			System.out.print("       ");
			for(int j=0;j<10;j++)
			{
				if(!arr3[i][j])
				{
					System.out.print("0  ");
				}
				else if(arr2[i][j]==0)
				{
					System.out.print("-  ");
				}
				else
				{
					System.out.print("X  ");
				}
			}
			System.out.println();
		}
		System.out.println("\nPlayer Score: " + sc1);
		System.out.println("AI Score: " + sc2 + "\n");
	}
	public static void printdeploy(int[][] arr)
	{
		System.out.println("\n\n");
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				if(arr[i][j]==0)
				{
					System.out.print("0  ");
				}
				else
				{
					System.out.print("*  ");
				}
			}
			System.out.println();
		}
		System.out.println("\n\n");
	}
}

class ai
{
	int count=0;
	int[][] board = new int[10][10];
	int[][] boardprob = new int[10][10];
	void initialise()
	{
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				board[i][j] = 0;
				boardprob[i][j] = 4000;
			}
		}
	}
	
	int grptwo()
	{
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<8;j++)
			{
				int sum = board[i][j] + board[i][j+1];
				if(sum==10)
				{
					return (10*i + j);
				}
			}
		}
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<8;j++)
			{
				int sum = board[j][i] + board[j+1][i];
				if(sum==10)
				{
					return (1000 + 10*j + i);
				}
			}
		}
		return -1;
	}

	int grpthr()
	{
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<8;j++)
			{
				int sum = board[i][j] + board[i][j+1] + board[i][j+2];
				if(sum==20 || sum==14 || sum==17)
				{
					if(board[i][j+1]!=0)
					{
						return (10*i + j);
					}
				}
			}
		}
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<8;j++)
			{
				int sum = board[j][i] + board[j+1][i] + board[j+2][i];
				if(sum==20 || sum==14 || sum==17)
				{
					if(board[j+1][i]!=0)
					{
						return (1000 + 10*j + i);
					}
				}
			}
		}
		return -1;
	}

	int decmove()
	{
		int bst2 = grpthr();
		if(bst2!=-1)
		{
			boolean flag = false;
			if(bst2/1000==1)
			{
				bst2 = bst2%1000;
				flag = true;
			}
			int r = bst2/10;
			int c = bst2%10;
			if(flag)
			{
				if(board[r][c]==0)
				{
					board[r+1][c] = 7;
					return (10*r + c);
				}
				else{
					board[r+1][c] = 7;
					return (10*(r+2) + c);
				}
			}
			else{
				if(board[r][c]==0)
				{
					board[r][c+1] = 7;
					return (10*r + c);
				}
				else
				{
					board[r][c+1] = 7;
					return (10*r + c+2);
				}
			}
		}
		int bst = grptwo();
		if(bst!=-1)
		{
			boolean flag = false;
			if(bst/1000==1)
			{
				bst = bst%1000;
				flag = true;
			}
			int r = bst/10;
			int c = bst%10;
			if(flag)
			{
				if(board[r][c]==0)
				{
					return (10*r + c);
				}
				else if(board[r+1][c]==0)
				{
					return (10*(r+1) + c);
				}
			}
			else{
				if(board[r][c]==0)
				{
					return (10*r + c);
				}
				else if(board[r][c+1]==0)
				{
					return (10*r + c+1);
				}	
			}
		}
		int max = 0;
		int ans = 0;
		Random an = new Random();
		int t=2;
		int turn = 0;
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				if(board[i][j]==1 || board[i][j]==10 || board[i][j]==7)
				{
					turn++;
				}
				if(boardprob[i][j]==max && board[i][j]==0)
				{
					int l = an.nextInt(t);
					if(l==0)
					{
						max = boardprob[i][j];
						ans = 10*i+j;
						t=2;
					}
					else
					{
						t++;
					}
				}
				if(boardprob[i][j]>max && board[i][j]==0)
				{
					max = boardprob[i][j];
					ans = 10*i + j;
				}
			}
		}
		if(turn<10)
		{
			int r = an.nextInt(10);
			int c = an.nextInt(10);
			while(board[r][c]==1 || board[r][c]==10 || board[r][c]==7)
			{
				r = an.nextInt(10);
				c = an.nextInt(10);
			}
			ans = 10*r + c;
		}
		return ans;
	}

	int chng(int a)
	{
		if(a>0)
		{
			return a-1;
		}
		else
		{
			return a+4;
		}
	}

	static int mod(int a, int b)
	{
		int ans = 0;
		if(a>b)
		{
			ans = a-b;
		}
		else
		{
			ans = b-a;
		}
		if(ans>10)
		{
			return 0;
		}
		else
		{
			return ans;
		}
	}

	void prop(int r, int c, int val)
	{
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				int dif = mod(i, r) + mod(j, c);
				boardprob[i][j] = boardprob[i][j] + dif*val;
			}
		}
	}

	void move(int r, int c, boolean hit)
	{
		if(hit)
		{
			board[r][c] = 10;
			prop(r, c, -1);
		}
		else
		{
			board[r][c] = 1;
			prop(r, c, 1);
		}
	}
}
