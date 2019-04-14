import java.util.Calendar;

public class Creditcard {
    private int id;
    private String name;
    private double fee;
    private int initiallimit;
    private int limit;
    private Calendar expiration;
    private int initialmoney;
    private int money;

    public Creditcard(int id,String name,double fee,int initiallimit,int limit,Calendar expiration,int initialmoney,int money)
    {
        this.id=id;
        this.name=name;
        this.fee=fee;
        this.initiallimit=initiallimit;
        this.limit=limit;
        this.expiration=expiration;
        this.initialmoney=initialmoney;
        this.money=money;
    }
    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getName(){return this.name;}
    public void setName(String name){this.name=name;}
    public double getFee()
    {
        return this.fee;
    }
    public void setFee(double fee)
    {
        this.fee=fee;

    }
    public int getInitiallimit()
    {
        return this.initiallimit;
    }
    public void setInitiallimit(int initiallimit)
    {
        this.initiallimit=initiallimit;
    }
    public int getLimit()
    {
        return this.limit;
    }
    public void setLimit(int limit)
    {
        this.limit=limit;
    }
    public Calendar getExpiration()
    {
        return this.expiration;
    }
    public void setExpiration(Calendar expiration)
    {
        this.expiration=expiration;
    }
    public int getMoney()
    {
        return this.money;
    }
    public void setMoney(int money)
    {
        this.money=money;
    }
    public int getInitialmoney()
    {
        return this.initialmoney;
    }
    public void setInitialmoney(int initialmoney)
    {
        this.initialmoney=initialmoney;
    }
    @Override
    public String toString()
    {
        return "Creditcard{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", fee='" + fee + '\'' +
                ", limit='" + limit + '\'' +
                ", expiration(month 0-11)='" + expiration.get(Calendar.DATE)+
                "."+expiration.get(Calendar.MONTH)+
                "."+expiration.get(Calendar.YEAR) + '\'' +
                ", money='" + money + '\'' +
                "} ";
    }
}
