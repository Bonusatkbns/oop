    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */
    package gameobject;

    /**
     *
     * @author Bonus
     */
    public class HP {
        private double maxHp;
        private double nowHp;
        public double getMaxhp() {
            return maxhp;
        }

        public void setMaxhp(double maxhp) {
            this.maxhp = maxhp;
        }

        public double getNowhp() {
            return nowhp;
        }
        public void setHP(double maxHp, double nowHp) {
            this.maxHp = maxHp;
            this.nowHp = nowHp;
        }
        public void setNowhp(double nowhp) {
            this.nowhp = nowhp;
        }

        public HP() {
        }

        public HP(double maxhp, double nowhp) {
            this.maxhp = maxhp;
            this.nowhp = nowhp;
        }
        private double maxhp;
        private double nowhp;

    }
