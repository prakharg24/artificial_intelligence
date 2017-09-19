import java.util.*;
import java.io.*;

public class bloc{
	public static void main(String args[]) throws Exception{
		Scanner in = new Scanner(System.in);


		System.out.println("\n\n\nSTART STATE");
		System.out.println("Enter s - the total number of towers in the start state :");
		int s = in.nextInt();

		System.out.println("\n\nFor each of next s lines, enter an integer n denoting the number of blocks in that tower followed by the blocks.");
		System.out.println();
		planner a = new planner();
		a.start();

		predicate m[] = new predicate[100];
		for(int i=0;i<100;i++){
			m[i] = new predicate();
		}

		int len = 0;
		for(int i=0;i<s;i++){
			int tem = in.nextInt();
			char last = in.next().charAt(0);
			m[len].a = last;
			m[len].b = '0';
			len++;
			for(int j=1;j<tem;j++){
				char curr = in.next().charAt(0);
				m[len].a = curr;
				m[len].b = last;
				len++;
				last = curr;
			}
			m[len].a = '0';
			m[len].b = last;
			len++;
		}

		a.lenpre = len;
		for(int i=0;i<len;i++){
			a.state[i].a = m[i].a;
			a.state[i].b = m[i].b;
		}

		System.out.println("\n\n\nGOAL STATE");
		System.out.println("Enter g - the total number of towers in the goal state :");
		int g = in.nextInt();

		System.out.println("\n\nFor each of next g lines, enter an integer n denoting the number of blocks in that tower followed by the blocks.");
		System.out.println();
		len = 0;
		for(int i=0;i<g;i++){
			int tem = in.nextInt();
			char last = in.next().charAt(0);
			m[len].a = last;
			m[len].b = '0';
			len++;
			for(int j=1;j<tem;j++){
				char curr = in.next().charAt(0);
				m[len].a = curr;
				m[len].b = last;
				len++;
				last = curr;
			}
			m[len].a = '0';
			m[len].b = last;
			len++;
		}

		predicate goal[] = new predicate[len];
		for(int i=0;i<len;i++){
			goal[i] = m[i];
		}

		a.totalplan(goal);
		a.printplan(); 
	}
}

class predicate{
	char a='0';
	char b='0';
	predicate next = null;
}

class stack{
	predicate top = null;
	void push(predicate a){
		if(top==null){
			top = a;
		}
		else{
			predicate temp = top;
			top = a;
			top.next = temp;
		}
	}
	void pop(){
		top = top.next;
	}
}

class plan{
	char a = '0';
	char b = '0';
	char c = '0';
}

class planner{
	predicate state[] = new predicate[100];
	plan steps[] = new plan[100];
	int lenpre = 0;
	int lenplan = 0;
	stack goals = new stack();
	void start(){
		for(int i=0;i<100;i++){
			state[i] = new predicate();
			steps[i] = new plan();
		}
	}
	boolean contain(predicate q){
		for(int i=0;i<lenpre;i++){
			if(state[i].a==q.a && state[i].b==q.b){
				return true;
			}
		}
		return false;
	}
	predicate[] diff(predicate[] goal){
		predicate ans[] = new predicate[100];
		int le = 0;
		for(int i=0;i<goal.length;i++){
			if(!contain(goal[i])){
				ans[le] = new predicate();
				ans[le] = goal[i];
				le++;
			}
		}
		predicate finalans[] = new predicate[le];
		for(int i=0;i<le;i++){
			finalans[i] = new predicate();
			finalans[i] = ans[i];
		}
		return finalans;
	}
	predicate[] precond(predicate p){
		predicate ans[] = new predicate[0];
		if(p.a=='0'){
			for(int i=0;i<lenpre;i++){
				if(state[i].a=='0' && state[i].b==p.b){
					return ans;
				}
				if(state[i].b==p.b){
					ans = new predicate[1];
					ans[0] = new predicate();
					ans[0].b = state[i].a;
					return ans;
				}
			}
			return ans;
		}
		else if(p.b=='0'){
			ans = new predicate[1];
			ans[0] = new predicate();
			ans[0].b = p.a;
			return ans;
		}
		else{
			ans = new predicate[2];
			ans[0] = new predicate();
			ans[1] = new predicate();
			ans[0].b = p.a;
			ans[1].b = p.b;
			return ans;
		}
	}
	boolean done(predicate p){
		if(p.a=='0'){
			int i;
			for(i=0;i<lenpre;i++){
				if(state[i].a=='0' && state[i].b==p.b){
					return true;
				}
				if(state[i].b==p.b){
					break;
				}
			}
			for(int j=0;j<lenpre;j++){
				if(state[j].a=='0' && state[j].b==state[i].a){
					state[lenpre].a = state[j].b;
					state[lenpre].b = '0';
					lenpre++;
					steps[lenplan].a = state[j].b;
					steps[lenplan].b = state[i].b;
					steps[lenplan].c = '0';
					lenplan++;
					state[i].a = '0';
					return true;
				}
			}
			return false;
		}
		else if(p.b=='0'){
			int i;
			for(i=0;i<lenpre;i++){
				if(state[i].a==p.a && state[i].b=='0'){
					return true;
				}
				if(state[i].a==p.a){
					break;
				}
			}
			for(int j=0;j<lenpre;j++){
				if(state[j].a=='0' && state[j].b==state[i].a){
					state[lenpre].a = state[j].b;
					state[lenpre].b = '0';
					lenpre++;
					steps[lenplan].a = state[j].b;
					steps[lenplan].b = state[i].b;
					steps[lenplan].c = '0';
					lenplan++;
					state[i].a = '0';
					return true;
				}
			}
			return false;			
		}
		else{
			int l=-1, m=-1;
			for(int i=0;i<lenpre;i++){
				if(state[i].a==p.a && state[i].b==p.b){
					return true;
				}
				if(state[i].b==p.a && state[i].a!='0'){
					return false;
				}
				if(state[i].b==p.b && state[i].a!='0'){
					return false;
				}
				if(state[i].a==p.a){
					l = i;
				}
				if(state[i].b==p.b){
					m = i;
				}
			}
			steps[lenplan].a = p.a;
			steps[lenplan].b = state[l].b;
			steps[lenplan].c = p.b;
			lenplan++;
			state[m].a = '0';
			state[m].b = state[l].b;
			state[l].b = p.b;
			return true;
		}
	}
	void totalplan(predicate[] goal){
		predicate ab[] = diff(goal);
		for(int i=0;i<ab.length;i++){
			goals.push(ab[i]);
		}
		while(goals.top!=null){
			if(done(goals.top)){
				goals.pop();
			}
			else{
				predicate tem[] = precond(goals.top);
				for(int i=0;i<tem.length;i++){
					goals.push(tem[i]);
				}
			}
			if(goals.top==null){
				ab = diff(goal);
				for(int i=0;i<ab.length;i++){
					goals.push(ab[i]);
				}
			}
		}
	}
	void printplan(){
		System.out.println("\n\n\nPLAN :\n");
		for(int i=0;i<lenplan;i++){
			if(steps[i].b=='0'){
				System.out.println("HOLD(" + steps[i].a + ")");
				System.out.println("STACK(" + steps[i].a + ", " + steps[i].c +")");
			}
			else if(steps[i].c=='0'){
				System.out.println("UNSTACK(" + steps[i].a + ", " + steps[i].b + ")");
				System.out.println("MVT(" +steps[i].a + ")");
			}
			else{
				System.out.println("UNSTACK(" + steps[i].a + ", " + steps[i].b + ")");
				System.out.println("STACK(" + steps[i].a + ", " + steps[i].c +")");
			}
		}
	}
}
