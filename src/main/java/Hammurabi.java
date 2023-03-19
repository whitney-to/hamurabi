import java.sql.SQLOutput;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Hammurabi {
    Random rand = new Random();  // this is an instance variable
    Scanner scanner = new Scanner(System.in);

    public static void main(String... args) { // required in every Java program
        new Hammurabi().playGame();
    }

    void playGame() {
        // declare local variables here: grain, population, etc.
        // statements go after the declations

        // initial variable
        int population = 100;
        int bushels = 2800;
        int acresOfLand = 1000; // Acres
        int bushelsPerAcre = 19; // bushels/acre

        System.out.format("\nIn year 0, the population is %d.\n"+
                "There are %d available bushels.\n"+
                "You have %d acres of land\n" +
                "The price is %d bushels per acre.\n\n",population,bushels,acresOfLand,bushelsPerAcre);

        for(int year = 1; year <= 10; year++){

            int acresToBuy = askHowManyAcresToBuy(bushelsPerAcre,bushels);
            acresOfLand += acresToBuy;
            bushels -= acresToBuy*bushelsPerAcre;

            if(acresToBuy==0){
                int acreToSell = askHowManyAcresToSell(acresOfLand);
                bushels += acreToSell*bushelsPerAcre;
                acresOfLand -= acreToSell;
            }

            int bushelsToFeedPeople = askHowMuchGrainToFeedPeople(bushels);
            bushels -= bushelsToFeedPeople;

            int acresToPlant = askHowManyAcresToPlant(acresOfLand,population,bushels);
            bushels -= acresToPlant*2;

            population -= plagueDeaths(population);

            int peopleStarved = starvationDeaths(population, bushelsToFeedPeople);
            population -= peopleStarved;

            if(uprising(population,peopleStarved)){
                System.out.println("TOO MANY PEOPLE DIED, YOU ARE IMPEACHED, BYE!!");

                break;
            }
            if(population==0){
                System.out.println("EVERYONE DIED, YOU HAVE SUBJECTS TO RULE, BYE!!");
                break;
            }

            int immigrant = immigrants(population,acresOfLand,bushels);
            population += immigrant;

            int bushelsHarvested = harvest(acresToPlant);
            int harvestRate = bushelsHarvested/acresToPlant;
            bushels += bushelsHarvested;

            int bushelsEatenByRats = grainEatenByRats(bushels);
            bushels -= bushelsEatenByRats;

            int newPrice = newCostOfLand();

            bushelsPerAcre = newPrice;

            printSummary(year,peopleStarved,immigrant,population,bushelsHarvested,
                    harvestRate,bushelsEatenByRats,bushels,acresOfLand,newPrice);
        }

    }

    // ----------------------------------------------------------------------------
    int  askHowManyAcresToBuy (int price, int bushels) {
        int acresAcquired = 0;
        while (true) {
            System.out.print("How many acres shall you buy? : ");
            try {
                acresAcquired = scanner.nextInt();
                if(acresAcquired*price <= bushels){
                    return acresAcquired;
                }
                System.out.print("You don't have enough bushels!!! ");
            }
            catch (InputMismatchException e) {
                System.out.print("\"" + scanner.next() + "\" isn't a number!");
            }
        }
    }

    int askHowManyAcresToSell(int acresToSell){
        int acresSold = 0;
        while (true) {
            System.out.print("How many acres shall you sell? : ");
            try {
                acresSold = scanner.nextInt();
                if(acresSold <= acresToSell){
                    return acresSold;
                }
                System.out.print("You don't have enough acres!!! ");
            }
            catch (InputMismatchException e) {
                System.out.print("\"" + scanner.next() + "\" isn't a number!");
            }
        }
    }

    int askHowMuchGrainToFeedPeople(int bushels){
        int bushelsToFeedPeople = 0;
        while (true) {
            System.out.print("How much grain you want to feed your people : ");
            try {
                bushelsToFeedPeople = scanner.nextInt();
                if(bushelsToFeedPeople <= bushels){
                    return bushelsToFeedPeople;
                }
                System.out.print("You don't have enough bushels!!! ");
            }
            catch (InputMismatchException e) {
                System.out.print("\"" + scanner.next() + "\" isn't a number!");
            }
        }
    }

    int askHowManyAcresToPlant(int acresOwned, int population, int bushels){
        int acresToPlant = 0;
        while (true) {
            System.out.print("How many acres you want to plant : ");
            try {
                acresToPlant = scanner.nextInt();
                if(acresToPlant <= acresOwned
                        && acresToPlant <= 2*bushels
                        && acresToPlant<= 10*population){
                    return acresToPlant;
                }
                System.out.print("You don't have enough acres or people or grain!!!");
            }
            catch (InputMismatchException e) {
                System.out.print("\"" + scanner.next() + "\" isn't a number!");
            }
        }
    }

    // ---------------------------------------------------------------------------------
    public int plagueDeaths(int population) {
        if(rand.nextInt(100)<15){
            return population/2;
        }
        return 0;
    }

    public int starvationDeaths(int population, int bushelsFedToPeople) {
        if(bushelsFedToPeople >= population*20){
            return 0;
        }
        return population - (bushelsFedToPeople-bushelsFedToPeople%20)/20;
    }

    public boolean uprising(int population, int howManyPeopleStarved) {
        if(howManyPeopleStarved >= population*0.45){
            return true;
        }
        return false;
    }


    public int immigrants(int population, int acresOwned, int grainInStorage) {
        return (20 * acresOwned + grainInStorage) / (100 * population) + 1;
    }

    public int harvest(int acres) {
        return acres*(rand.nextInt(6)+1);
    }

    public int grainEatenByRats(int bushels) {
        if(rand.nextInt(100)<=40){
            return (int) Math.ceil((bushels * (rand.nextInt(21)+10) /100.0));
        }
        return 0;
        }

    public int newCostOfLand() {
        return rand.nextInt(7)+17;
    }

    void printSummary(int year,
                      int peopleStarved,
                      int peopleCame,
                      int population,
                      int bushelsHarvested,
                      int bushelsPerAcre,
                      int bushelsDestroyedByRats,
                      int leftOverBushelsAfterRats,
                      int acresOfLand,
                      int price){
        int temp = 0; // for fill in correct var later
        System.out.format("------------------------------------------------------------\n");
        System.out.format("O great Hammurabi!\n");
        System.out.format("You are in year %d of your ten year rule.\n", year);
        System.out.format("In the previous year %d people starved to death.\n",peopleStarved);
        System.out.format("In the previous year %d people entered the kingdom.\n",peopleCame);
        System.out.format("The population is now %d.\n",population);
        System.out.format("We harvested %d bushels at %d bushels per acre.\n",bushelsHarvested,bushelsPerAcre);
        System.out.format("Rats destroyed %d bushels, leaving %d bushels in storage.\n",bushelsDestroyedByRats,leftOverBushelsAfterRats);
        System.out.format("The city owns %d acres of land.\n",acresOfLand);
        System.out.format("Land is currently worth %d bushels per acre.\n",price);
        System.out.format("------------------------------------------------------------\n");
    }

    void finalSummary(boolean isWon){

    }
    //other methods go here
}
