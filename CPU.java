public class CPU {


    CPU()
    {
        setTimer(0);
        this.CurrentP=new Process();
    }

    public Process CurrentP;
    public  int Timer;

    public void setCurrentP(Process currentP) {
        CurrentP = currentP;
    }

    public void setTimer(int timer) {
        Timer = timer;
    }
}
