import java.util.*;
import java.io.*;

public class assignment1
{
	static int loc[][]=new int[28][3];
	static int magic[][][] = new int[3][3][3];
	static int non_col_size=0;
	static int make_2_len=0;
	static int canwin_len = 0;
	static int trip=12;
	static int tr[][];
	static int pmov =0;
	static int cmov =0;
	static int parr[] = new int[20];
	static int carr[] = new int[20];
	static int cube[][][] = new int[3][3][3];
	static int pwin=0;
	static int cwin=0;

	//position determines for us the type of position i.e. corner, edge centre or surface centre

	static int position(int p)
	{
		if(magic[0][0][0]==p)
			return 1;
		if(magic[0][0][1]==p)
			return 3;
		if(magic[0][0][2]==p)
			return 1;
		if(magic[0][1][0]==p)
			return 3;
		if(magic[0][1][1]==p)
			return 2;
		if(magic[0][1][2]==p)
			return 3;
		if(magic[0][2][0]==p)
			return 1;
		if(magic[0][2][1]==p)
			return 3;
		if(magic[0][2][2]==p)
			return 1;
		if(magic[1][0][0]==p)
			return 3;
		if(magic[1][0][1]==p)
			return 2;
		if(magic[1][0][2]==p)
			return 3;
		if(magic[1][1][0]==p)
			return 2;
		if(magic[1][1][1]==p)
			return 0;
		if(magic[1][1][2]==p)
			return 2;
		if(magic[1][2][0]==p)
			return 3;
		if(magic[1][2][1]==p)
			return 2;
		if(magic[1][2][2]==p)
			return 3;
		if(magic[2][0][0]==p)
			return 1;
		if(magic[2][0][1]==p)
			return 3;
		if(magic[2][0][2]==p)
			return 1;
		if(magic[2][1][0]==p)
			return 3;
		if(magic[2][1][1]==p)
			return 2;
		if(magic[2][1][2]==p)
			return 3;
		if(magic[2][2][0]==p)
			return 1;
		if(magic[2][2][1]==p)
			return 3;
		if(magic[2][2][2]==p)
			return 1;
		return 4;

	}

	//myfunc is used to recursively generate the magic cube

	static void myfunc(int arr[][][], int l, int r, int c, int value, int n)
	{
		int l1=l-1,l2 = l+1,r1=r-1,r2=r+1,c1=c-1,c2=c+1;
		
		if(l==0)
			l1=n-1;
		if(l==n-1)
			l2=0;

		if(r==0)
			r1=n-1;

		if(r==n-1)
			r2=0;


		if(c==0)
			c1=n-1;
		if(c==n-1)
			c2=0;

		if(arr[l1][r][c1]==0)
		{
			arr[l1][r][c1]=value;
			loc[value][0]=l1;
			loc[value][1]=r;
			loc[value][2]=c1;

			myfunc(arr,l1,r,c1,value+1,n);
		}
		else if(arr[l1][r1][c]==0)
		{
			arr[l1][r1][c]=value;
			loc[value][0]=l1;
			loc[value][1]=r1;
			loc[value][2]=c;

			myfunc(arr,l1,r1,c,value+1,n);
		}
		else if(arr[l2][r][c]==0)
		{
			arr[l2][r][c]=value;
			loc[value][0]=l2;
			loc[value][1]=r;
			loc[value][2]=c;

			myfunc(arr,l2,r,c,value+1,n);
		}
		
	}

	//cross helps us determine whether the points given are collinear or not

	static boolean cross(int a1, int a2, int a3, int b1, int b2, int b3, int c1, int c2, int c3)
	{
	    int x1 = b1 - a1;
	    int x2 = c1 - a1;
	    int y1 = b2 - a2;
	    int y2 = c2 - a2;
	    int z1 = b3 - a3;
	    int z2 = c3 - a3;
	    if((y1*z2 - z1*y2 == 0)&&(z1*x2 - x1*z2 == 0)&&(x1*y2 - y1*x2 == 0))
	    {
	      return true;
	    }

	    else
	    {
	      return false;
	    }
		
	}

	//make2 helps us determine the move for making two consecutive spots in the grid

	static int[] make2(int x, int y, int z)
	{
		int xm = x-1, xp = x+1, ym = y-1, yp = y+1, zm = z-1, zp = z+1;

		int ans[] = new int[26];
		int len = 0;

		int xc = xm;
		int yc = ym;
		int zc = zm;
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				for(int k=0;k<3;k++){
					if(xc <3 && xc >-1 && yc<3 && yc>-1 && zc<3 && zc>-1)
					{
						if(!(xc==x && yc==y && zc==z)){
							int or[] = win(magic[x][y][z], magic[xc][yc][zc]);
							if(or[0] != -1)
							{
								if(cube[xc][yc][zc]==0 && cube[or[0]][or[1]][or[2]]==0){
									ans[len] = magic[xc][yc][zc];
									len++;
								}
							}
						}
					}
					if(k==0)
						zc = z;
					else
						zc = zp;
				}
				zc = zm;
				if(j==0)
					yc = y;
				else
					yc = yp;
			}
			yc = ym;
			if(i==0)
				xc = x;
			else
				xc = xp;
		}
		int finalans[] = new int[len];
		for(int i=0;i<len;i++){
			finalans[i] = ans[i];
		}
		make_2_len=len;
		return finalans;
	
	}

	// win lets us know the point on which if we play we can win

	static int[] win(int p1, int p2)
	{
		int p3 = 42 - p1 - p2;
		int ans[] = new int[3];
		ans[0] = -1;
		ans[1] = -1;
		ans[2] = -1;
		if(p3>0 && p3<28){
			if(cross(loc[p1][0], loc[p1][1],loc[p1][2],loc[p2][0], loc[p2][1],loc[p2][2],loc[p3][0], loc[p3][1],loc[p3][2]))
			{
				ans[0] = loc[p3][0];
				ans[1] = loc[p3][1];
				ans[2] = loc[p3][2];
			}
		}
		if(notft(p1,p2) !=0){
				int t = notft(p1, p2);
				ans[0] = loc[t][0];
				ans[1] = loc[t][1];
				ans[2] = loc[t][2];
		}
		return ans;
	
	}

	//notft checks from the list in which the sum is not 42, but the points are still collinear

	static int notft(int a, int b)
	{
		int arr[] = new int[2];
		arr[0] = a;
		arr[1] = b;
		Arrays.sort(arr);
		for(int i=0;i<trip;i++){
			if(tr[i][0] == arr[0] && tr[i][1] == arr[1]){
				return tr[i][2];
			}
			else if(tr[i][0] == arr[0] && tr[i][2] == arr[1]){
				return tr[i][1];
			}
			else if(tr[i][1] == arr[0] && tr[i][2] == arr[1]){
				return tr[i][0];
			}
		}
		return 0;
	
	}

	//canwin_inter tells us the best winning position for the computer

	static int[] canwin_inter(int p)
	{
		int canw[][]=canwin(p);
		int i,j,k;

		int counter[]=new int[28];
		for(i=0;i<28;i++)
		{
			counter[i]=0;
		}

		for(i=0;i<canwin_len;i++)
		{
			counter[magic[canw[i][0]][canw[i][1]][canw[i][2]]]++;
		}

		int maxx=0;
		int val=0;
		for(i=1;i<28;i++)
		{
			if(maxx<counter[i])
			{
				maxx=counter[i];
				val=i;
			}

		}
		int aa[]=new int[2];
		aa[0]=val;
		aa[1]=maxx;

		return aa;
	}

	//canwin_inter2 will tell us the number of wins that will be obtained on doinga a move

	static int canwin_inter2(int p, int pp)
	{
		int canw[][]=canwin(p);
		int i,j,k;

		int counter[]=new int[28];
		for(i=0;i<28;i++)
		{
			counter[i]=0;
		}
		for(i=0;i<canwin_len;i++)
		{
			counter[magic[canw[i][0]][canw[i][1]][canw[i][2]]]++;
		}
		int maxx = 0;
		maxx=counter[pp];

		return maxx;
	}

	// the driver function for all movements

	static int move()
	{
	 	int temc[] = canwin_inter(2);
	  	int temp[] = canwin_inter(1);

		if(cube[1][1][1]==0)
		{
			cube[1][1][1] = 2;
			carr[cmov] = magic[1][1][1];
			cmov++;
			return 0;
		}
	  	else if(temc[0] != 0)
	  	{
			cube[ loc[temc[0]][0] ][ loc[temc[0]][1] ][ loc[temc[0]][2]  ] = 2;
			carr[cmov] = magic[ loc[temc[0]][0] ][ loc[temc[0]][1] ][ loc[temc[0]][2]  ];
			cmov++;
			return temc[1];
		}
	 	else if(temp[0] != 0)
	 	{
	 		cube[ loc[temp[0]][0] ][ loc[temp[0]][1] ][ loc[temp[0]][2]  ] = 2;
			carr[cmov] = magic[ loc[temp[0]][0] ][ loc[temp[0]][1] ][ loc[temp[0]][2]  ];
			cmov++;
			return 0;
	  	}
	 	else
	 	{
		  	int tem = inter();
		  	cube[loc[tem][0]][loc[tem][1]][loc[tem][2]] = 2;
		  	carr[cmov] = magic[loc[tem][0]][loc[tem][1]][loc[tem][2]];
		  	cmov++;
		  	return 0;
	  	}
	}

	//canwin_inter calls canwin to get all possible winning positions

	static int[][] canwin(int p)
	{
		int mov;
	  	int arr[];
		if(p==1)
		{
			mov = pmov;
	    	arr = parr;
		}
		else
		{
			mov = cmov;
	    	arr = carr;
		}

	  	int ans[][] = new int[mov][3];
	  	int len =0;
		for(int i=0;i<mov;i++)
		{
		    for(int j=i+1;j<mov;j++)
		    {
			    int[] temp = win(arr[i], arr[j]);
			    if(temp[0] != -1 && cube[temp[0]][temp[1]][temp[2]]==0)
			    {
			      	ans[len][0] = temp[0];
			        ans[len][1] = temp[1];
			        ans[len][2] = temp[2];
			        len++;
			    }
		    }
		}

	  	int finalans[][];
	 	if(len==0)
	 	{
		    finalans = new int[1][3];
		    finalans[0][0] = -1;
		    finalans[0][1] = -1;
		    finalans[0][2] = -1;
	  	}

	 	else
	 	{
	    	finalans = new int[len][3];
		  	for(int i=0;i<len;i++)
		  	{
		  		finalans[i][0] = ans[i][0];
		    	finalans[i][1] = ans[i][1];
		  		finalans[i][2] = ans[i][2];
	  		}
	  	}
	  	canwin_len = len;
	  	return finalans;
	
	}

	//non_collinear makes the list of all combinations which are not collinear but the sum of all of them is 42

	static int[][] non_collinear()
	{
		int i,j,k,l;
		int temp[][]=new int[2925][3];
		int size=0;


		for(i=1;i<28;i++)
		{
			for(j=i;j<28;j++)
			{
				for(k=j;k<28;k++)
				{
					if(cross(loc[i][0],loc[i][1], loc[i][2], loc[j][0], loc[j][1], loc[j][2], loc[k][0],loc[k][1], loc[k][2])==false && (i+j+k)==42)
					{
						temp[size][0]=i;
						temp[size][1]=j;
						temp[size][2]=k;
						size++;
					}
				}
			}
		}

		int ans[][] = new int[size][3];
		for(i=0;i<size;i++)
		{
			ans[i][0] = temp[i][0];
		  	ans[i][1] = temp[i][1];
		  	ans[i][2] = temp[i][2];
		}

		non_col_size=size;
		return ans;

	}

	//this heps us determine the best make2 option for maximum score possibilities

	static int inter()
	{
	  int[] moves=carr;
	  int number=cmov;
	  
	  int myarr[]=new int[28];
	  int i, j,k;
	  for(i=1;i<28;i++)
	  {
	  	myarr[i]=0;
	  }
	  
	  for(i=0;i<number;i++)
	  {
		int store[]=make2(loc[moves[i]][0], loc[moves[i]][1], loc[moves[i]][2]);

		for(j=0;j<make_2_len;j++)
		{
			myarr[store[j]]++;
		}
		  	
	  }
	  int maxi=0;
	  int val=0;
	  for(i=1;i<28;i++)
	  {
	  	if(myarr[i]>=maxi && cube[loc[i][0]][loc[i][1]][loc[i][2]] == 0)
	  	{
	  		val=i;
	  		maxi=myarr[i];
	  	}
	  }
	  int[] bestindices=new int[28];
	  for(i=1;i<28;i++)
	  {
	  	bestindices[i]=0;
	  }

	  int bestlen=0;
	  for(i=1;i<28;i++)
	  {
	  	if(myarr[i]==maxi && cube[loc[i][0]][loc[i][1]][loc[i][2]] == 0)
	  	{
	  		bestindices[bestlen]=i;
	  		bestlen++;
	  	}
	  }

	  int minpri = 4;
	  val = 0;
	  for(i =0;i<bestlen;i++)
	  {
	  	k = position(bestindices[i]);
	  	if(k<minpri)
	  	{
	  		minpri = k;
	  		val = bestindices[i];
	  	}
	  }
	  return val;
	  
	}

	//trip_finder makes the list of all triplets whose sum is not 42 but they are collinear

	static void trip_finder()
	{
		tr = new int[12][3];

		tr[0][0] = magic[0][0][0];
		tr[0][1] = magic[0][1][1];
		tr[0][2] = magic[0][2][2];

		tr[1][0] = magic[0][0][2];
		tr[1][1] = magic[0][1][1];
		tr[1][2] = magic[0][2][0];

		tr[2][0] = magic[0][0][0];
		tr[2][1] = magic[1][0][1];
		tr[2][2] = magic[2][0][2];

		tr[3][0] = magic[0][0][2];
		tr[3][1] = magic[1][0][1];
		tr[3][2] = magic[2][0][0];

		tr[4][0] = magic[0][2][0];
		tr[4][1] = magic[1][2][1];
		tr[4][2] = magic[2][2][2];

		tr[5][0] = magic[0][2][2];
		tr[5][1] = magic[1][2][1];
		tr[5][2] = magic[2][2][0];

		tr[6][0] = magic[2][0][0];
		tr[6][1] = magic[2][1][1];
		tr[6][2] = magic[2][2][2];

		tr[7][0] = magic[2][0][2];
		tr[7][1] = magic[2][1][1];
		tr[7][2] = magic[2][2][0];

		tr[8][0] = magic[0][0][2];
		tr[8][1] = magic[1][1][2];
		tr[8][2] = magic[2][2][2];

		tr[9][0] = magic[0][2][2];
		tr[9][1] = magic[1][1][2];
		tr[9][2] = magic[2][0][2];
		
		tr[10][0] = magic[0][0][0];
		tr[10][1] = magic[1][1][0];
		tr[10][2] = magic[2][2][0];
		
		tr[11][0] = magic[0][2][0];
		tr[11][1] = magic[1][1][0];
		tr[11][2] = magic[2][0][0];

		for(int i=0;i<12;i++){
			int tempa[] = new int[3];
			tempa[0] = tr[i][0];
			tempa[1] = tr[i][1];
			tempa[2] = tr[i][2];
			Arrays.sort(tempa);
			tr[i][0] = tempa[0];
			tr[i][1] = tempa[1];
			tr[i][2] = tempa[2];
		}
	}

	//common condition checking for different modes

	static boolean checkwhile(int pwin, int cwin, int pmov, int cmov, int mode){
		int a, b, c, d;

		if(mode==1){
			a = pwin;
			b = 0;
			c = cwin;
			d = 0;
		}
		else if(mode==2){
			a = pwin;
			b = 1;
			c = cwin;
			d = 1;
		}
		else{
			a = pmov+cmov;
			b = 20;
			c = 4;
			d = 5;
		}

		if(a<=b && c<=d){
			return true;
		}
		else{
			return false;
		}

	}

	//common condition checking for different modes
	static boolean checkif(int pwin, int cwin, int pmov, int cmov, int mode){
		int a, b, c, d;

		if(mode==1){
			a = pwin;
			b = 0;
			c = cwin;
			d = 0;
		}
		else if(mode==2){
			a = pwin;
			b = 1;
			c = cwin;
			d = 1;
		}
		else{
			a = pmov+cmov;
			b = 20;
			c = 4;
			d = 5;
		}

		if(a>b || c>d){
			return true;
		}
		else{
			return false;
		}

	}

	//prints all the surfaces of the cube

	static void printsurface(){
		int i, j, k;
		int n=3;
		System.out.println("Top Surface");
		for(i=0;i<n;i++)
		{
			for(j=0;j<n;j++)
			{
					System.out.print(magic[i][j][0]);
					System.out.print(" ");
			}
			System.out.println();
		}

		System.out.println();System.out.println();System.out.println();

		System.out.println("Bottom Surface");
		for(i=0;i<n;i++)
		{
			for(j=0;j<n;j++)
			{
					System.out.print(magic[i][j][2]);
					System.out.print(" ");
			}
			System.out.println();
		}

		System.out.println();System.out.println();System.out.println();
		System.out.println("Left Surface");
		for(j=0;j<n;j++)
			{
				for(k=0;k<n;k++)
				{
					System.out.print(magic[0][j][k]);
					System.out.print(" ");
				}
				System.out.println();
			}
		System.out.println();System.out.println();System.out.println();

		System.out.println("Right Surface");
		for(j=0;j<n;j++)
		{
			for(k=0;k<n;k++)
			{
				System.out.print(magic[2][j][k]);
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println();System.out.println();System.out.println();

		System.out.println("Front Surface");
		for(i=0;i<n;i++)
		{
				for(k=0;k<n;k++)
				{
					System.out.print(magic[i][2][k]);
					System.out.print(" ");
				}
				System.out.println();
			}
			System.out.println();System.out.println();System.out.println();

		System.out.println("Back Surface");
		for(i=0;i<n;i++)
		{
				for(k=0;k<n;k++)
				{
					System.out.print(magic[i][0][k]);
					System.out.print(" ");
				}
				System.out.println();
			}
			System.out.println();System.out.println();System.out.println();

		System.out.println("Middle-front Surface");
		for(i=0;i<n;i++)
		{
				for(k=0;k<n;k++)
				{
					System.out.print(magic[i][1][k]);
					System.out.print(" ");
				}
				System.out.println();
			}
			System.out.println();System.out.println();System.out.println();

		System.out.println("Middle-left Surface");
			for(j=0;j<n;j++)
			{
				for(k=0;k<n;k++)
				{
					System.out.print(magic[1][j][k]);
					System.out.print(" ");
				}
				System.out.println();
			}
			System.out.println();System.out.println();System.out.println();

		System.out.println("Middle-top Surface");
		for(i=0;i<n;i++)
		{
			for(j=0;j<n;j++)
			{
					System.out.print(magic[i][j][1]);
					System.out.print(" ");
				}
				System.out.println();
			}
			System.out.println();System.out.println();System.out.println();

		System.out.println("Diagonal Surface 1");
		System.out.println(magic[0][0][0] + " " + magic[1][1][0] + " " + magic[2][2][0]);
		System.out.println(magic[0][0][1] + " " + magic[1][1][1] + " " + magic[2][2][1]);
		System.out.println(magic[0][0][1] + " " + magic[1][1][2] + " " + magic[2][2][2]);

			System.out.println();System.out.println();System.out.println();

		System.out.println("Diagonal Surface 2");
		System.out.println(magic[2][0][0] + " " + magic[1][1][0] + " " + magic[0][2][0]);
		System.out.println(magic[2][0][1] + " " + magic[1][1][1] + " " + magic[0][2][1]);
		System.out.println(magic[2][0][2] + " " + magic[1][1][2] + " " + magic[0][2][2]);

			System.out.println();System.out.println();System.out.println();

	}

	public static void main(String args[])
	{
		Scanner in = new Scanner(System.in);
		int n=3;
		int arr[][][]=new int[n][n][n];
		int i,j,k,m;

		
		for(i=0;i<28;i++)
		{
			for(j=0;j<3;j++)
			{
				loc[i][j]=0;
			}
		}


		for(i=0;i<n;i++)
		{
			for(j=0;j<n;j++)
			{
				for(k=0;k<n;k++)
				{
					arr[i][j][k]=0;
				}
			}
		}

		arr[0][(n-1)/2][(n-1)/2]=1;
		
		loc[1][0]=0;
		loc[1][1]=(n-1)/2;
		loc[1][2]=(n-1)/2;

		assignment1.myfunc(arr,0,(n-1)/2,(n-1)/2,2,n);

		magic = arr;


		int testi[][] = non_collinear();

		trip_finder();

		System.out.println("\n\n\n\n");

		System.out.println("What do you want to do?? \n\n");
		System.out.println("A -> Display generated Magic Cube");
		System.out.println("B -> Display non-collinear triplets with sum 42");
		System.out.println("C -> Display collinear triplets with sum not 42");
		System.out.println("D -> Play 3-D Tic Tac Toe");

		String wh = in.next();
		while((wh.charAt(0) != 'A' && wh.charAt(0) != 'B' && wh.charAt(0) != 'C' && wh.charAt(0) != 'D') && wh.length()!=1){
			System.out.println("Not a valid input. Try Again: ");
			wh = in.next();
		}

		if(wh.charAt(0)=='A'){
			printsurface();
			return;
		}
		if(wh.charAt(0)=='B'){
			for(i=0;i<non_col_size;i++){
				System.out.println(testi[i][0] + " " + testi[i][1] + " " + testi[i][2]);
			}
			return;
		}
		if(wh.charAt(0)=='C'){
			for(i=0;i<12;i++){
				System.out.println(tr[i][0] + " " + tr[i][1] + " " + tr[i][2]);
			}
			return;
		}

		System.out.println("GAME MODE: ");
		System.out.println("------------------------------------------------------------");
		System.out.println("Mode 1 -> First player to make one collinear line wins");
		System.out.println("Mode 2 -> First player to make two collinear lines wins");
		System.out.println("Mode 3 -> The player with most collinear lines after 20 moves wins");
		System.out.println("------------------------------------------------------------");
		System.out.println("Enter 1/2/3 to select the mode: ");
		int mode = in.nextInt();
		while(mode!=1 && mode!=2 && mode!=3){
			System.out.println("Please enter a valid mode: ");
			mode = in.nextInt();
		}
		int flag = 0;
		boolean first = true;

		System.out.println("Enter \"f\" to play first:");

		String meri = in.next();
		if(meri.charAt(0)=='f' && meri.length() == 1){
			first = false;
		}
		System.out.println("------------------------------------------------------------");
		System.out.println("INSTRUCTIONS:");
		System.out.println("------------------------------------------------------------");
		System.out.println("0 -> Empty cell");
		System.out.println("1 -> Cell filled by the player");
		System.out.println("2 -> Cell filled by the AI");
		System.out.println("\n\n");
		System.out.println("Representation: ");
		System.out.println("x -> Increases left to right in a layer");
		System.out.println("y -> Increases top to bottom in a layer");
		System.out.println("z -> Increases at the change of layer from top to bottom");
		System.out.println("------------------------------------------------------------");

		System.out.println("GAME STARTS: ");
		System.out.println("------------------------------------------------------------");

		System.out.println("Enter anything to start: ");
		String kuch = in.next();

		if(first){
			flag = move();
			int tempo = carr[cmov-1];
			System.out.println("Computer played : " + loc[tempo][0] + " " + loc[tempo][1] + " " + loc[tempo][2]);
		}

		while(checkwhile(pwin, cwin, pmov, cmov, mode)){
			System.out.println("\n\nTotal Moves Played : " + (pmov+cmov));
			System.out.println("Cube: \n");
			for(i=0;i<n;i++)
			{
				for(j=0;j<n;j++)
				{
					for(k=0;k<n;k++)
					{
						System.out.print(cube[k][j][i]);
						System.out.print(" ");
							
					}
					System.out.println();
				}
				System.out.println();System.out.println();
			}


			System.out.println("cwin : " + cwin);
			System.out.println("pwin : " + pwin);

			System.out.println("Enter x and y and z: ");
			int x = in.nextInt();
			int y = in.nextInt();
			int z = in.nextInt();
			while(x>2 || x<0 || y>2 || y<0 || z>2 || z<0 || cube[x][y][z]!=0){
				System.out.println("Already filled or Invalid Coordinates. Please enter again: ");
				x = in.nextInt();
				y = in.nextInt();
				z = in.nextInt();
			}
			flag = canwin_inter2(1, magic[x][y][z]);
			if(flag>0)
			{
				pwin = pwin + flag;
			}
			cube[x][y][z] = 1;
			parr[pmov] = magic[x][y][z];
			pmov++;

			if(checkif(pwin, cwin, pmov, cmov, mode)){
				break;
			}

			flag = move();

			int tempo2 = carr[cmov-1];
			System.out.println("Computer played : " + loc[tempo2][0] + " " + loc[tempo2][1] + " " + loc[tempo2][2]);


			if(flag>0)
			{
				cwin = cwin + flag;
			}


		}

		if(pwin>cwin)
		{
			System.out.println("PLAYER WINS!!");
		}
		else if(pwin<cwin)
		{
			System.out.println("COMPUTER WINS!!");
		}
		else
		{
			System.out.println("DRAW!!");
		}


	}
}






























