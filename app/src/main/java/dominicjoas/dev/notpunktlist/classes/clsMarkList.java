package dominicjoas.dev.notpunktlist.classes;

import static dominicjoas.dev.notpunktlist.classes.clsMarkList.Mode.abitur;
import java.text.DecimalFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Dominic Joas
 * @version 0.0.1
 */
public class clsMarkList {
    private int intMaxPoints;
    private double dblWorstAt = 0.0, dblBestAt;
    private Entry<Double, Double> entryPKT;
    private double dblMultiplyMarks = 0.1, dblMultiplyPoints = 0.25;
    private boolean boolDictatMode = false;
    public boolean boolNamedMarksIsEnabled = false, boolAbiturPointsIsEnabled = false;
    private List<Double> generatedList;
    
    public clsMarkList(int maxPoints) {
        this.setMaxPoints(maxPoints);
    }
    
    public boolean setMaxPoints(int maxPoints) {
        try {
            if(maxPoints>=1) {
                if(maxPoints>=4000) {
                    this.intMaxPoints = 20;
                    return false;
                } else {
                    this.intMaxPoints = maxPoints;
                    return true;
                }
            } else {
                this.intMaxPoints = 20;
                return false;
            }
        } catch(Exception ex) {
            this.intMaxPoints = 20;
            return false;
        } finally {
            this.dblBestAt = (double) this.intMaxPoints;
        }
    }
    
    public int getMaxPoints() {
        return this.intMaxPoints;
    }
    
    public void setDictatMode(boolean dictatMode) {
        this.boolDictatMode = dictatMode;
    }
    
    public boolean isDictatModeEnabled() {
        return this.boolDictatMode;
    }
    
    public void setPointsMultiplier(PointsMultiplier multi) {
        switch(multi) {
            case quarterPoints:
                this.dblMultiplyPoints = 0.25;
                break;
            case halfPoints:
                this.dblMultiplyPoints = 0.5;
                break;
            case wholePoints:
                this.dblMultiplyPoints = 1.0;
                break;
            default:
                this.dblMultiplyPoints = 1.0;
        }
    }
    
    public double getPointsMultiplier() {
        return this.dblMultiplyPoints;
    }
    
    public void setMarkMultiplier(MarkMultiplier multi) {
        switch(multi) {
            case tenthMarks:
                this.dblMultiplyMarks = 0.1;
                this.boolNamedMarksIsEnabled = false;
                this.boolAbiturPointsIsEnabled = false;
                break;
            case quarterMarks:
                this.dblMultiplyMarks = 0.25;
                this.boolNamedMarksIsEnabled = false;
                this.boolAbiturPointsIsEnabled = false;
                break;
            case namedMarks:
                this.dblMultiplyMarks = 0.25;
                this.boolNamedMarksIsEnabled = true;
                this.boolAbiturPointsIsEnabled = false;
                break;
            case wholeMarks:
                this.dblMultiplyMarks = 1.0;
                this.boolNamedMarksIsEnabled = false;
                this.boolAbiturPointsIsEnabled = false;
                break;
            case abiturpoints:
                this.dblMultiplyMarks = 1.0;
                this.boolNamedMarksIsEnabled = false;
                this.boolAbiturPointsIsEnabled = true;
                break;
            default:
                this.dblMultiplyMarks = 0.1;
                this.boolNamedMarksIsEnabled = false;
                this.boolAbiturPointsIsEnabled = false;
        }
    }
    
    public double getMarkMultiplier() {
        return this.dblMultiplyMarks;
    }
    
    public boolean setPoint(double points, double mark) {
        if(points>this.intMaxPoints || points <= 0 || points<=this.dblWorstAt || points>=this.dblBestAt) {
            this.entryPKT = new AbstractMap.SimpleEntry<>((double)this.dblBestAt-(this.dblBestAt-this.dblWorstAt)/2, 3.5);
            return false;
        } else if(mark>6 || mark < 1) {
            this.entryPKT = new AbstractMap.SimpleEntry<>((double)this.dblBestAt-(this.dblBestAt-this.dblWorstAt)/2, 3.5);
            return false;
        } else {
            this.entryPKT = new AbstractMap.SimpleEntry<>(points, mark);
            return true;
        }
    }
    
    public Entry getPoint() {
        return this.entryPKT;
    }
    
    public boolean setBestAt(double points) {
        if(points>this.intMaxPoints || points<=(1+this.dblWorstAt) || points < 2) {
            this.dblBestAt = this.intMaxPoints;
            return false;
        } else {
            this.dblBestAt = points;
            return true;
        }
    }
    
    public double getBestAt() {
        return this.dblBestAt;
    }
    
    public boolean setWorstAt(double points) {
        if(points<0 || points>=(this.dblBestAt-1) || points > 18) {
            this.dblWorstAt = 0;
            return false;
        } else {
            this.dblWorstAt = points;
            return true;
        }
    }
    
    public double getWorstAt() {
        return this.dblWorstAt;
    }
    
    public Map generateList(Sorting srt, Mode mode) {
        Map<Double, Double> marklist = new LinkedHashMap<>();
        if(srt==Sorting.bestMarkFirst || srt==Sorting.worstMarkFirst) {
            this.generatedList = this.generateMarkList();
        } else {
            this.generatedList = this.generatePointList(this.intMaxPoints);
        }
        switch(mode) {
            case linear:
                if(this.boolAbiturPointsIsEnabled) {
                    marklist = this.generateList(srt, abitur);
                    break;
                }
                for(double curVal :this.generatedList) {
                    if(srt==Sorting.bestMarkFirst || srt==Sorting.worstMarkFirst) {
                        marklist.put(curVal, this.closest(this.calcPoints(curVal), this.generatePointList(this.intMaxPoints)));
                    } else {
                        marklist.put(curVal, this.closest(this.calcMark(curVal), this.generateMarkList()));
                    }
                }
                break;
            case exponential:
                if(this.boolAbiturPointsIsEnabled) {
                    marklist = this.generateList(srt, abitur);
                    break;
                }
                double x1 = this.dblWorstAt, x2 = (double) this.dblBestAt, y1 = 6.0, y2 = 1.0;
                if(srt==Sorting.bestMarkFirst || srt==Sorting.worstMarkFirst) {
                    List<Double> points = this.generatePointList(this.intMaxPoints);
                    for(double curval : generatedList) {
                        double sqrt = Math.pow((y2/y1), 1/(x2-x1));
                        marklist.put(curval, this.closest(Math.log(curval/(y1/Math.pow(sqrt, x1)))/Math.log(sqrt), points));
                    }
                } else {
                    List<Double> marks = this.generateMarkList();
                    for(double curval : generatedList) {
                        double sqrt = Math.pow((y2/y1), 1/(x2-x1));
                        marklist.put(curval, this.closest((y1/Math.pow(sqrt, x1))*Math.pow(sqrt, curval), marks));
                    }
                }
                break;
            case withCrease:
                if(this.boolAbiturPointsIsEnabled) {
                    marklist = this.generateList(srt, abitur);
                    break;
                }
                marklist = this.generateListWithCrease(marklist, srt);
                break;
            case ihk:
                if(this.boolAbiturPointsIsEnabled) {
                    marklist = this.generateList(srt, abitur);
                    break;
                }
                Map<Double, Double> mpMain = this.getIHKList();
                if(srt==Sorting.bestMarkFirst || srt==Sorting.worstMarkFirst) {
                    for(Map.Entry<Double, Double> curEntry : mpMain.entrySet()) {
                        marklist.put(curEntry.getValue(), this.closest(((double)this.intMaxPoints/100) * curEntry.getKey(), this.generatePointList(this.intMaxPoints)));
                    }
                } else {
                    List<Double> lsValues = new ArrayList<>();
                    for(double entry : mpMain.keySet()) {
                        lsValues.add(entry);
                    }
                    for(double curVal :this.generatedList) {
                        marklist.put(curVal, mpMain.get(this.closest(curVal/((double)this.intMaxPoints/100), lsValues)));
                    }
                }
                break;
            case abitur:
                mpMain = this.getAbiturPointList();
                if(srt==Sorting.bestMarkFirst || srt==Sorting.worstMarkFirst) {
                    for(Map.Entry<Double, Double> curEntry : mpMain.entrySet()) {
                        marklist.put(curEntry.getValue(), this.closest(((double)this.intMaxPoints/100) * curEntry.getKey(), this.generatePointList(this.intMaxPoints)));
                    }
                } else {
                    List<Double> lsValues = new ArrayList<>();
                    for(double entry : mpMain.keySet()) {
                        lsValues.add(entry);
                    }
                    for(double curVal :this.generatedList) {
                        marklist.put(curVal, mpMain.get(this.closest(curVal/((double)this.intMaxPoints/100), lsValues)));
                    }
                }
                break;
            default:
                
        }
        if(this.boolDictatMode) {
            Map<Double, Double> tmp = new LinkedHashMap<>();
            if(srt==Sorting.bestMarkFirst || srt==Sorting.worstMarkFirst) {
                for(Map.Entry<Double, Double> entry : marklist.entrySet()) {
                    tmp.put(entry.getKey(), this.intMaxPoints-entry.getValue());
                }
            } else {
                for(Map.Entry<Double, Double> entry : marklist.entrySet()) {
                    tmp.put(this.intMaxPoints-entry.getKey(), entry.getValue());
                }
            }
            marklist = tmp;
        }
        if(srt == Sorting.highestPointsFirst || srt == Sorting.worstMarkFirst) {
            Map<Double, Double> tmp = new LinkedHashMap<>();
            List<Double> keys = new ArrayList<>();
            for(double curKey : marklist.keySet()) {
                keys.add(curKey);
            }
            Collections.reverse(keys);
            for(double curVal : keys) {
                tmp.put(curVal, marklist.get(curVal));
            }
            marklist = tmp;
        }
        if(this.boolNamedMarksIsEnabled) {
            return this.replaceWithName(marklist, srt);
        }
        return marklist;
    }
    
    public double calcMark(double actPoints) {
        double actMark = actPoints * 5;
        actMark = actMark / this.intMaxPoints;
        actMark = actMark - 6;
        actMark = actMark * -1;
        return Double.parseDouble(new DecimalFormat("0.00").format(actMark).replace(",", "."));
    }
    
    public double calcMark(double actPoints, double max) {
        double actMark = actPoints * 5;
        actMark = actMark / max;
        actMark = actMark - 6;
        actMark = actMark * -1;
        return Double.parseDouble(new DecimalFormat("0.00").format(actMark).replace(",", "."));
    }
    
    public double calcPoints(double actMark) {
        double actPoints = actMark - 6;
        actPoints *= this.intMaxPoints;
        actPoints /= 5;
        if(actPoints<0) {
            actPoints *= -1;
        }
        return Double.parseDouble(new DecimalFormat("0.00").format(actPoints).replace(",", "."));
    }
    
    public double calcPoints(double actMark, double max) {
        double actPoints = actMark - 6;
        actPoints *= max;
        actPoints /= 5;
        if(actPoints<0) {
            actPoints *= -1;
        }
        return Double.parseDouble(new DecimalFormat("0.00").format(actPoints).replace(",", "."));
    }
    
    public enum MarkMultiplier {
        tenthMarks,
        quarterMarks,
        namedMarks,
        wholeMarks,
        abiturpoints
    }
    
    public enum PointsMultiplier {
        quarterPoints,
        halfPoints,
        wholePoints
    }
    
    public enum Sorting {
        bestMarkFirst,
        worstMarkFirst,
        highestPointsFirst,
        lowestPointsFirst
    }
    
    public enum Mode {
        linear,
        exponential,
        withCrease,
        ihk,
        abitur
    }
    
    private List<Double> generatePointList(double maxPoints) {
        List<Double> lstGiven = new ArrayList<>();
        for(double i = 0.0; i<=maxPoints; i = i + this.dblMultiplyPoints) {
            lstGiven.add(Double.parseDouble(new DecimalFormat("0.00").format(i).replace(",", ".")));
        }
        return lstGiven;
    }
    
    private List<Double> generateMarkList() {
        List<Double> lstGiven = new ArrayList<>();
        if(!this.boolAbiturPointsIsEnabled) {
            for(double i = 1.0; i<=6.0; i = i + this.dblMultiplyMarks) {
                lstGiven.add(Double.parseDouble(new DecimalFormat("0.00").format(i).replace(",", ".")));
            }
        } else {
            for(double i = 15.0; i>=0.0; i--) {
                lstGiven.add(Double.parseDouble(new DecimalFormat("0.00").format(i).replace(",", ".")));
            }
        }
        return lstGiven;
    }
    
    private double closest(double searchedMark, List<Double> in) {
        double min = Double.MAX_VALUE;
        double closest = searchedMark;
        for (double actMark : in) {
            if(searchedMark>actMark) {
                if(min>(searchedMark-actMark)) {
                    min = searchedMark - actMark;
                    closest = actMark;
                }
            } else {
                if(min>(actMark-searchedMark)) {
                    min = actMark - searchedMark;
                    closest = actMark;
                }
            }
        }

        return closest;
    }
    
    private Map<String, String> replaceWithName(Map<Double, Double> currentList, Sorting srt) {
        Map<String, String> mapTemp = new LinkedHashMap<>();
        if(srt==Sorting.bestMarkFirst||srt==Sorting.worstMarkFirst) {
            for(double key : currentList.keySet()) {
                String strKey = String.valueOf(key);
                int whole = Integer.parseInt(strKey.replace(".", "-").split("-")[0]);
                strKey = strKey.replace(".5", "-" + (whole+1));
                strKey = strKey.replace(whole + ".75", (whole+1)+"+");
                strKey = strKey.replace(".25", "-");
                strKey = strKey.replace(".0", "");
                mapTemp.put(strKey, currentList.get(key).toString());
            }
        } else {
            for(double key : currentList.keySet()) {
                String strKey = String.valueOf(currentList.get(key));
                if(strKey.contains(".")) {
                    int whole = Integer.parseInt(strKey.replace(".", "-").split("-")[0]);
                    strKey = strKey.replace(".5", "-" + (whole+1));
                    strKey = strKey.replace(whole + ".75", (whole+1)+"+");
                    strKey = strKey.replace(".25", "-");
                    strKey = strKey.replace(".0", "");
                }
                mapTemp.put(String.valueOf(key), strKey);
            }
        }
        return mapTemp;
    }
    
    private Map<Double, Double> generateListWithCrease(Map<Double, Double> mpMain, Sorting srt) {
        if(srt==Sorting.bestMarkFirst || srt==Sorting.worstMarkFirst) {
            List<Double> lsCurrentMarkList = this.generateMarkList();
            Map<Double, Double> mp = new LinkedHashMap<>();
            mp = this.generateListWithCrease(mp, Sorting.lowestPointsFirst);
            Map<Double, Double> mpReverse = reverse(mp);
            List<Double> lsTMP = new ArrayList<>();
            for(double key : mpReverse.keySet()) {
                lsTMP.add(key);
            }
            for(double value : lsCurrentMarkList) {
                mpMain.put(value, mpReverse.get(closest(value, lsTMP)));
            }
        } else {
            List<Double> lsCurrentMarkList = this.generateMarkList();
            double currentPoints = 0.0;
            for(;currentPoints<=this.dblWorstAt;currentPoints = currentPoints + this.dblMultiplyPoints) {
                mpMain.put(currentPoints, 6.0);
            }
            for(int i = 1;currentPoints<=this.entryPKT.getKey();currentPoints += this.dblMultiplyPoints, i++) {
                double dblCurrentY = 6-(i*((6-this.entryPKT.getValue())/((this.entryPKT.getKey()-this.dblWorstAt)/this.dblMultiplyPoints)));
                mpMain.put(currentPoints, closest(dblCurrentY, lsCurrentMarkList));
            }
            for(;currentPoints<this.dblBestAt;currentPoints += this.dblMultiplyPoints) {
                double dblCurrentX = this.dblBestAt - currentPoints;
                double dblCurrentY = (dblCurrentX * ((this.entryPKT.getValue()-1)/((this.dblBestAt-this.entryPKT.getKey())/this.dblMultiplyPoints))) + 1;
                mpMain.put(currentPoints, closest(dblCurrentY, lsCurrentMarkList));
            }
            for(;currentPoints<=this.intMaxPoints; currentPoints += this.dblMultiplyPoints)
                mpMain.put(currentPoints, 1.0);
        }
        return mpMain;
    }
    
    public static <K,V> Map<V,K> reverse(Map<K,V> map) {
        Map<V,K> rev = new LinkedHashMap<>();
        for(Map.Entry<K,V> entry : map.entrySet())
            rev.put(entry.getValue(), entry.getKey());
        return rev;
    }
    
    private Map<Double, Double> getAbiturPointList() {
        Map<Double, Double> mapAbitur = new LinkedHashMap<>();
        mapAbitur.put(95.0, 15.0);
        mapAbitur.put(90.0, 14.0);
        mapAbitur.put(85.0, 13.0);
        mapAbitur.put(80.0, 12.0);
        mapAbitur.put(75.0, 11.0);
        mapAbitur.put(70.0, 10.0);
        mapAbitur.put(65.0, 9.0);
        mapAbitur.put(60.0, 8.0);
        mapAbitur.put(55.0, 7.0);
        mapAbitur.put(50.0, 6.0);
        mapAbitur.put(45.0, 5.0);
        mapAbitur.put(39.0, 4.0);
        mapAbitur.put(33.0, 3.0);
        mapAbitur.put(27.0, 2.0);
        mapAbitur.put(20.0, 1.0);
        mapAbitur.put(0.0, 0.0);
        return mapAbitur;
    }

    private Map<Double, Double> getIHKList() {
        Map<Double, Double> mapAbitur = new LinkedHashMap<>();
        mapAbitur.put(100.0, 1.0);
        mapAbitur.put(99.0, 1.1);
        mapAbitur.put(97.0, 1.2);
        mapAbitur.put(95.0, 1.3);
        mapAbitur.put(93.0, 1.4);
        mapAbitur.put(91.0, 1.5);
        mapAbitur.put(90.0, 1.6);
        mapAbitur.put(89.0, 1.7);
        mapAbitur.put(88.0, 1.8);
        mapAbitur.put(87.0, 1.9);
        mapAbitur.put(86.0, 2.0);
        mapAbitur.put(84.0, 2.1);
        mapAbitur.put(83.0, 2.2);
        mapAbitur.put(82.0, 2.3);
        mapAbitur.put(81.0, 2.4);
        mapAbitur.put(80.0, 2.5);
        mapAbitur.put(79.0, 2.6);
        mapAbitur.put(78.0, 2.7);
        mapAbitur.put(76.0, 2.8);
        mapAbitur.put(75.0, 2.9);
        mapAbitur.put(73.0, 3.0);
        mapAbitur.put(72.0, 3.1);
        mapAbitur.put(70.0, 4.2);
        mapAbitur.put(69.0, 3.3);
        mapAbitur.put(67.0, 3.4);
        mapAbitur.put(66.0, 3.5);
        mapAbitur.put(65.0, 3.6);
        mapAbitur.put(63.0, 3.7);
        mapAbitur.put(61.0, 3.8);
        mapAbitur.put(60.0, 3.9);
        mapAbitur.put(58.0, 4.0);
        mapAbitur.put(56.0, 4.1);
        mapAbitur.put(54.0, 4.2);
        mapAbitur.put(53.0, 4.3);
        mapAbitur.put(51.0, 4.4);
        mapAbitur.put(49.0, 4.5);
        mapAbitur.put(48.0, 4.6);
        mapAbitur.put(46.0, 4.7);
        mapAbitur.put(44.0, 4.8);
        mapAbitur.put(42.0, 4.9);
        mapAbitur.put(40.0, 5.0);
        mapAbitur.put(37.0, 5.1);
        mapAbitur.put(35.0, 5.2);
        mapAbitur.put(33.0, 5.3);
        mapAbitur.put(31.0, 5.4);
        mapAbitur.put(29.0, 5.5);
        mapAbitur.put(28.0, 5.6);
        mapAbitur.put(22.0, 5.7);
        mapAbitur.put(16.0, 5.8);
        mapAbitur.put(11.0, 5.9);
        mapAbitur.put(5.0, 6.0);
        return mapAbitur;
    }
}
