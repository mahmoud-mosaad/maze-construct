package buildmaze;

import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class BuildMaze {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), m = sc.nextInt();         
        Maze maze = new Maze(n , m);
        maze.build();
        maze.display();        
    }
}

class UFWeighted{
    int [] p = null;
    int [] sz = null;
    int N;
    UFWeighted(){N=0;}
    UFWeighted(int n){N = n ; this.Init();}
    void Init(){
        p = new int [N];
        sz = new int [N];
        for(int i = 0 ; i < N ; p[i] = i , sz[i++] = 1);
    }

    int Root(int i)
    {
        for( ; p[i] != i ; p[i] = p[ p[i] ] , i = p[i] ); // with path compression
        return i;
    }

    void Union(int A ,int B) // weighted union
    {
        int root_A = Root(A);
        int root_B = Root(B);

        if (root_A == root_B) return;
        
        if(sz[root_A] < sz[root_B ])
        {
            p[ root_A ] = p[root_B];
            sz[root_B] += sz[root_A];
        }
        else
        {
            p[ root_B ] = p[root_A];
            sz[root_A] += sz[root_B];
        }
    }

    boolean Find(int A,int B)
    {
        return ( Root(A)==Root(B) )? true : false;
    }
}

class Pair{
    int first , second;
    Pair(int f , int s){
        first = f;second = s;
    }
}

class Maze{

    int rows , columns , N , N2 , currentEdge=0;
    int counter = 0;
    Vector<Pair> maze = new Vector<Pair>();
    Vector<Pair> removes = new Vector<Pair>();
    UFWeighted unionFind;
    Pair [] edges = null ;
    boolean [] exist = null ;

    Maze(){rows = 0 ; columns = 0;}
    Maze(int n ,int m){rows = n ; columns = m ; this.init();}

    void init(){
        N = rows * columns;
        unionFind = new UFWeighted(N);
        N2 = (columns-1)*rows + columns*(rows-1);
        edges = new Pair [N2];
        exist = new boolean [N2];
        for(int i=0;i<N2;i++) exist[i] = false;
    }

    Pair getRandomEdge(){
        Random Rand = new Random();
        int r = Rand.nextInt(N2-1);
        while(exist[r] ==  true) r = Rand.nextInt(N2-1);
        exist[r] = true;
        return edges[r];
    }

    Pair make_pair(int f, int s){
        return new Pair(f,s);
    }
    
    void fillEdges(){
        for(int i=0;i<N-columns;i++){
            edges[currentEdge++] = this.make_pair(i , i+columns);
            maze.add(edges[currentEdge-1]);
        }
        for(int i=0;i<N;i++){
            if ((i+1) % columns != 0){
                edges[currentEdge++] = make_pair(i , i+1);
                maze.add(edges[currentEdge-1]);
            }
        }
    }
    
    boolean doRemove(int s , int d){
        for(int i=0;i<removes.size();i++){
            if (removes.get(i).first == s && removes.get(i).second == d)
                return true;
        }
        return false;
    }
    
    void build()
    {
        this.fillEdges();
        while (/*unionFind.Root(0) != unionFind.Root(N-1)*/ counter < N - 1 ){
            Pair edg = getRandomEdge();
            boolean f = unionFind.Find(edg.first , edg.second);
            if (f == false){
                unionFind.Union(edg.first  , edg.second);
                counter++;
                //System.out.println("Remove : "+edg.first+" "+edg.second);
                removes.add(make_pair(edg.first , edg.second));
            }
        }
    }

    void display(){
        boolean [] rmcols = new boolean [columns];
        for(int i=0;i<columns;i++)
            System.out.print("+---"); 
        System.out.println("+");
        for(int i=0;i<N;i++){
            
            if (i > 0 && i % columns == 0){
                for(int j=0;j<columns;j++){
                    System.out.print("+"); 
                    if (rmcols[j] == true)
                        System.out.print("   "); 
                    else
                        System.out.print("---"); 
                }
                System.out.println("+");
            }
            
            if (i < N-columns && doRemove(i , i+columns)){
                rmcols[i%columns] = true;
            }   
            
            if (i % columns == 0){
                if (i > 0)
                    System.out.print("|");
                else
                    System.out.print(" ");
                
            }
            
            if ((i+1) % columns == 0){
                if (i != N-1)
                    System.out.println("   |");
                else
                    System.out.println("    ");
                
            }
            
            if ((i+1) % columns != 0 && doRemove(i , i+1)){
                System.out.print("    ");
            }
            else if ((i+1) % columns != 0 && !doRemove(i , i+1)){
                System.out.print("   |");
            }
            
        }
        for(int i=0;i<columns;i++)
            System.out.print("+---"); 
        System.out.println("+");
    }
    
}

