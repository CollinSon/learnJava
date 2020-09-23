package com.github.conllinson.learnenum;/**
 * @author: 许瑞锐
 * @Date: 2020/7/23 16:00
 */

/**
 * @date 2020/7/23 16:00
 * @author 许瑞锐
 * @description {java类描述}
 */
public class Pizza {

    private PizzaStatus pizzaStatus;

    public PizzaStatus getPizzaStatus() {
        return pizzaStatus;
    }

    public void setPizzaStatus(PizzaStatus pizzaStatus) {
        this.pizzaStatus = pizzaStatus;
    }

    public enum PizzaStatus{


        ORDERED(5){
            @Override
            public boolean isOrdered() {
                return true;
            }
        },
        READY(2){
            @Override
            public boolean isReady() {
                return true;
            }
        },
        DELIVERED(0){
            @Override
            public boolean isDelivered() {
                return true;
            }
        };

        private int timeToDelivery;


        public boolean isOrdered() {return false;}

        public boolean isReady() {return false;}

        public boolean isDelivered(){return false;}



        PizzaStatus(int timeToDelivery) {
            this.timeToDelivery = timeToDelivery;
        }

        public int getTimeToDelivery() {
            return timeToDelivery;
        }
    }

    public static void main(String[] args) {
        Pizza testPz = new Pizza();
        testPz.setPizzaStatus(Pizza.PizzaStatus.READY);
    }


}
