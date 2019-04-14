import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Solve {
    private List<Integer> initial;
    private List<Atm> allAtms;
    private List<Creditcard> allCreditcards;
    private Calendar currentDate;
    private Calendar newDate;
    private LocalTime currentTime;
    private Calendar targetDate;
    private LocalTime targetTime;
    private int targetMoney;
    private int currentMoney;
    private int initialAtmLimit;
    private List<Integer> visited;

    //Initialization and hard-codation of all data (works for any other input data)
    public Solve()
    {
        this.visited=new ArrayList<>();
        this.allAtms=new ArrayList<>();
        this.allCreditcards=new ArrayList<>();
        this.currentMoney=0;

        //add the distances from the initial location A to all the ATMs
        this.initial=new ArrayList<>();
        this.initial.add(5);
        this.initial.add(60);
        this.initial.add(30);
        this.initial.add(45);

        //create all the ATM instances
        List<Integer> distances1= List.of(0,40,40,45);
        LocalTime open1=LocalTime.of(12,0);
        LocalTime close1=LocalTime.of(18,0);
        Atm atm1=new Atm(1,open1,close1,distances1);

        List<Integer> distances2= List.of(40,0,15,30);
        LocalTime open2=LocalTime.of(10,0);
        LocalTime close2=LocalTime.of(17,0);
        Atm atm2=new Atm(2,open2,close2,distances2);

        List<Integer> distances3= List.of(40,15,0,15);
        LocalTime open3=LocalTime.of(22,0);
        LocalTime close3=LocalTime.of(12,0);
        Atm atm3=new Atm(3,open3,close3,distances3);

        List<Integer> distances4= List.of(45,30,15,0);
        LocalTime open4=LocalTime.of(17,0);
        LocalTime close4=LocalTime.of(1,0);
        Atm atm4=new Atm(4,open4,close4,distances4);

        //add the ATMs to the list of all ATMs
        this.allAtms.add(atm1);
        this.allAtms.add(atm2);
        this.allAtms.add(atm3);
        this.allAtms.add(atm4);

        //create all the Creditcard(id, name, fee, initial limit, limit,date, initial money, money) instances
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, 2020);
        date.set(Calendar.MONTH, 5);
        date.set(Calendar.DAY_OF_MONTH, 23);
        Creditcard card1=new Creditcard(1,"SILVER",0.2,4500,4500, date,20000,20000);
        this.allCreditcards.add(card1);

        Calendar date1 = Calendar.getInstance();
        date1.set(Calendar.YEAR, 2018);
        date1.set(Calendar.MONTH, 8);
        date1.set(Calendar.DAY_OF_MONTH, 15);
        Creditcard card2=new Creditcard(2,"GOLD",0.1,3000,3000, date1,25000,25000);
        this.allCreditcards.add(card2);

        Calendar date2 = Calendar.getInstance();
        date2.set(Calendar.YEAR, 2019);
        date2.set(Calendar.MONTH, 3);
        date2.set(Calendar.DAY_OF_MONTH, 20);
        Creditcard card3=new Creditcard(3,"PLATINUM",0,4000,4000, date2,3000,3000);
        this.allCreditcards.add(card3);

        //the credit cards are sorted ascending by fee
        this.allCreditcards.sort(Comparator.comparing(Creditcard::getFee));

        //current date
        Calendar date3 = Calendar.getInstance();
        date3.set(Calendar.YEAR, 2019);
        date3.set(Calendar.MONTH, 3);
        date3.set(Calendar.DAY_OF_MONTH, 19);
        this.currentDate=date3;

        //target date
        Calendar date4 = Calendar.getInstance();
        date4.set(Calendar.YEAR, 2019);
        date4.set(Calendar.MONTH, 3);
        date4.set(Calendar.DAY_OF_MONTH, 19);
        this.targetDate=date4;

        //current time
        LocalTime curr=LocalTime.of(11,30);
        this.currentTime=curr;

        //target time
        LocalTime targ=LocalTime.of(14,0);
        this.targetTime=targ;

        //target money to be withdrawn
        this.targetMoney=7500;

        //money limit in each ATM
        this.initialAtmLimit=5000;

        //new date is a clone of current date which will be used to determine if the clock passed 00:00
        this.newDate=(Calendar)this.currentDate.clone();
    }

    //this function starts the iteration with the list of distances from the starting location A to all the other ATMs
    public List<Atm> getAtmsRoute(){
        if (this.currentDate.equals(this.targetDate) && this.currentTime.compareTo(this.targetTime)>0)
            System.out.println("invalid input data");
        else {
            this.iteration(this.initial);
        }
        List<Atm> atmsroute=new ArrayList<>();
        for (int i:this.visited)
            for (Atm atm:this.allAtms)
                if (atm.getId()==i) {
                    atmsroute.add(atm);
                    break;
                }
        return atmsroute;
    }

    //this function takes a list of distances from current location to the other ATMs and return the valid atm that is
    //closer to the current location(this increases the chances that the user will be able to withdraw the required sum
    // of money in the target time)
    //in:List distances
    //out:Atm selectedAtm
    public Atm getBestAtm(List<Integer> dist) {
        Atm currentatm = new Atm();
        Atm selectedatm = new Atm();
        int min = 9999;
        int index=-1;
        for (int i : dist) {
            index++;
            if (i<min && i!=0) {
                currentatm = this.allAtms.get(index);
                LocalTime aux = this.currentTime.plusMinutes(i);
                //this is the case when the schedule of an ATM lies on 2 days(i.e. 22->12)
                if(currentatm.getOpenTime().compareTo(currentatm.getCloseTime())>0) {
                    //if aux is between current atm open time and 00:00
                    if (aux.compareTo(currentatm.getOpenTime()) >= 0 && aux.compareTo(currentatm.getCloseTime()) > 0) {
                        if (!this.visited.contains(currentatm.getId()) && this.currentMoney < this.targetMoney
                                && aux.compareTo(this.targetTime) < 0){
                            min=i;
                            this.visited.add(currentatm.getId());
                            selectedatm=currentatm;
                        }
                        if (!this.visited.contains(currentatm.getId()) && this.currentMoney < this.targetMoney
                                && aux.compareTo(this.targetTime) > 0 && this.targetDate.after(this.currentDate)){
                            min=i;
                            this.visited.add(currentatm.getId());
                            selectedatm=currentatm;
                        }
                    }
                    //if aux is between 00:00 and current atm close time
                    if (aux.compareTo(currentatm.getOpenTime()) < 0 && aux.compareTo(currentatm.getCloseTime()) < 0) {
                        if (!this.visited.contains(currentatm.getId()) && this.currentMoney < this.targetMoney
                                && aux.compareTo(this.targetTime) < 0){
                            min=i;
                            this.visited.add(currentatm.getId());
                            selectedatm=currentatm;
                        }
                    }
                }
                else {
                    //this is the normal schedule case
                    if (!this.visited.contains(currentatm.getId()) && this.currentMoney < this.targetMoney
                            && aux.compareTo(this.targetTime) < 0 && aux.compareTo(currentatm.getCloseTime()) < 0
                            && aux.compareTo(currentatm.getOpenTime()) >= 0) {
                        min = i;
                        this.visited.add(currentatm.getId());
                        selectedatm = currentatm;
                    }
                }
            }
        }
        //return only if selectedatm is valid
        if (selectedatm.getId()!=0) {
            LocalTime timebefore=this.currentTime;
            this.currentTime = this.currentTime.plusMinutes(min);
            if (this.currentTime.compareTo(timebefore)<0) {
                this.newDate.add(Calendar.DATE, 1);
            }
            return selectedatm;
        }
        else
            return null;
    }

    public void iteration(List<Integer> dist){
        //we take the best ATM, given the list of distances
        Atm currentatm=new Atm();
        currentatm=this.getBestAtm(dist);
        //if time passes 00:00 reset the withdraw limit of each credit card to the initial limit
        if (this.newDate.after(this.currentDate)) {
            for (Creditcard c : this.allCreditcards)
                c.setLimit(c.getInitiallimit());
            this.currentDate=(Calendar)this.newDate.clone();
        }
        if (currentatm!=null) {
            int atmLimit = this.initialAtmLimit;
            for (Creditcard c : this.allCreditcards)
                if (c.getExpiration().after(this.currentDate) && atmLimit > 0 && c.getMoney() > 0 && c.getLimit()>0) {
                    atmLimit = this.verification(c, currentatm, atmLimit);
                }
            this.iteration(currentatm.getDistances());
        }
    }

    //function that makes the verifications and validations for each case
    //in:Creditcard c, Atm atm, int atmLimit
    //out:int atmLimit (the atm limit after all the possibilities were taken into account)
    public int verification(Creditcard c,Atm atm, int atmLimit){
        if (c.getMoney() < c.getLimit()) {
            if (c.getMoney() < atmLimit) {
                if ((this.currentMoney + c.getMoney()) < this.targetMoney) {
                    System.out.println("atm:" + atm.getId() + " sum:" + c.getMoney() + " card:" + c.getName());
                    atmLimit -= c.getMoney();
                    c.setLimit(c.getLimit() - c.getMoney());
                    this.currentMoney += c.getMoney();
                    c.setMoney(0);
                }
                if ((this.currentMoney + c.getMoney()) >= this.targetMoney) {
                    int money = this.targetMoney - this.currentMoney;
                    System.out.println("atm:" + atm.getId() + " sum:" + money + " card:" + c.getName());
                    atmLimit -= money;
                    c.setLimit(c.getLimit() - money);
                    this.currentMoney += money;
                    c.setMoney(c.getMoney() - money);
                }
            }
            if (c.getMoney() > atmLimit) {
                if ((this.currentMoney + atmLimit) < this.targetMoney) {
                    System.out.println("atm:" + atm.getId() + " sum:" + atmLimit + " card:" + c.getName());
                    this.currentMoney += atmLimit;
                    c.setLimit(c.getLimit() - atmLimit);
                    c.setMoney(c.getMoney() - atmLimit);
                    atmLimit -= atmLimit;
                }
                if ((this.currentMoney + atmLimit) >= this.targetMoney) {
                    int money = this.targetMoney - this.currentMoney;
                    System.out.println("atm:" + atm.getId() + " sum:" + money + " card:" + c.getName());
                    this.currentMoney += money;
                    c.setLimit(c.getLimit() + money);
                    c.setMoney(c.getMoney() - money);
                    atmLimit -= money;
                }
            }
        }
        if (c.getMoney() > c.getLimit()) {
            if (c.getLimit() < atmLimit) {
                if ((this.currentMoney + c.getLimit()) < this.targetMoney) {
                    System.out.println("atm:" + atm.getId() + " sum:" + c.getLimit() + " card:" + c.getName());
                    atmLimit -= c.getLimit();
                    this.currentMoney += c.getLimit();
                    c.setMoney(c.getMoney() - c.getLimit());
                    c.setLimit(0);
                }
                if ((this.currentMoney + c.getLimit()) >= this.targetMoney) {
                    int money = this.targetMoney - this.currentMoney;
                    System.out.println("atm:" + atm.getId() + " sum:" + money + " card:" + c.getName());
                    atmLimit -= money;
                    c.setLimit(c.getLimit() - money);
                    this.currentMoney += money;
                    c.setMoney(c.getMoney() - money);
                }
            }
            if (c.getLimit() > atmLimit) {
                if ((this.currentMoney + atmLimit) < this.targetMoney) {
                    System.out.println("atm:" + atm.getId() + " sum:" + atmLimit + " card:" + c.getName());
                    this.currentMoney += atmLimit;
                    c.setLimit(c.getLimit() - atmLimit);
                    c.setMoney(c.getMoney() - atmLimit);
                    atmLimit -= atmLimit;
                }
                if ((this.currentMoney + atmLimit) >= this.targetMoney) {
                    int money = this.targetMoney - this.currentMoney;
                    System.out.println("atm:" + atm.getId() + " sum:" + money + " card:" + c.getName());
                    this.currentMoney += money;
                    c.setLimit(c.getLimit() - money);
                    c.setMoney(c.getMoney() - money);
                    atmLimit -= money;
                }
            }
        }
        return atmLimit;
    }
}
