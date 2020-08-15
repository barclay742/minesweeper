import org.junit.Test;

import static org.junit.Assert.*;

public class SquareTest {
    @Test
    public void Constructor(){
        Square s = new Square();

    }

    @Test
    public void NeighboursTest(){
        Square[][] squareTest = new Square[5][5];
        for(int j = 0; j<squareTest.length;j++){
            for(int i =0; i<squareTest.length;i++){
                if(i+j==3){
                squareTest[j][i] = new Square(i,j,1,1);}
                else{
                squareTest[j][i] = new Square(i,j,1,0);}
            }
        }
        Square test1 = new Square(1,1,1,0);
        assert(test1.Neighbours(squareTest)==2);


        Square[][] squareTest2 = new Square[5][5];
        for(int j = 0; j<squareTest2.length;j++){
            for(int i =0; i<squareTest2.length;i++){
                if(i+j==4){
                    squareTest2[j][i] = new Square(i,j,1,1);}
                else{
                    squareTest2[j][i] = new Square(i,j,1,0);}
            }
        }
        Square test2 = new Square(0,4,1,0);
        assert(test2.Neighbours(squareTest2)==1);
    }

}