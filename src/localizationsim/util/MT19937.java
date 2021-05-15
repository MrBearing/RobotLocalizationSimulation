package localizationsim.util;

/**
 * 32bit版メルセンヌ・ツイスター生成器
 * 
 * @note [参考]Javaによるアルゴリズム辞典
 *              奥村晴彦＋首藤一幸+杉浦方紀+土村展之+津留和生+細田隆之+松井吉光+光成滋生[著]
 *              技術評論社 P.448, 2003
 * 
 * @author tomono
 */
public class MT19937 extends java.util.Random{
    /**
     * 
     */
    private static final long serialVersionUID = 6813206303126859644L;
	
    static final int N = 624;
    static final int M = 397;
    static final int UPPER_MASK = 0x80000000;
    static final int LOWER_MASK = 0x7fffffff;
    static final int MATRIX_A   = 0x9908b0Df;
    int x[] = new int[N];
    int p, q, r;
    
    public MT19937(){
        setSeed(System.currentTimeMillis());
    }
    
    public MT19937(long seed){
        setSeed(seed);
    }
    
    public MT19937(int[] seeds){
        setSeed(seeds);
    }
    
    synchronized public void setSeed(long seed){
        if (x==null) return; //superからの呼び出しでは何もしない
        x[0] = (int)seed;
        for (int i=1; i<N; i++){
            x[i] = 1812433253 * (x[i-1] ^ x[i-1]>>>30)+i;
        }
        p=0; q=1; r=M;
    }
    
    synchronized public void setSeed(int[] seeds){
        setSeed(19650218);
        int i=1, j=0;
        for (int k=0; k<Math.max(N, seeds.length); k++){
            x[i] ^= (x[i-1] ^ x[i-1]>>>30)*1664525;
            x[i] += seeds[j]+j;
            if (++i >= N){
                x[0] = x[N-1];
                i=1;
            }
            if (++j >= seeds.length){
                j=0;
            }            
        }
        for (int k=0; k<N-1; k++){
            x[i] ^= (x[i-1] ^ x[i-1] >>> 30)*1566083941;
            x[i] -= i;
            if (++i >= N){
                x[0] = x[N-1];
                i=1;
            }
        }
        x[0] = 0x80000000;
    }
    
    @Override
    synchronized protected int next(int bits){
        int y = (x[p] & UPPER_MASK) | (x[q] & LOWER_MASK);
        y = x[p] = x[r] ^ (y>>>1) ^ ((y&1)*MATRIX_A);
        if (++p == N){ p=0; }
        if (++q == N){ q=0; }
        if (++r == N){ r=0; }
        
        y ^= (y >>> 11);
        y ^= (y <<  7) & 0x9d2c5680;
        y ^= (y << 15) & 0xefc60000;
        y ^= (y >>> 18);
        return (y >>> (32-bits));
    }
}