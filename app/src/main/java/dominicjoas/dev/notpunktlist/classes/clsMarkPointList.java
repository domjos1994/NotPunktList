package dominicjoas.dev.notpunktlist.classes;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author Dominic Joas
 * @version 1.0
 */
public class clsMarkPointList {
    private int intMaxPoints;
    private float fltMarkMultiplier, fltPointsMultiplier, fltBestMarkAt, fltWorstMarkTo, fltCustomPoints, fltCustomMark;
    private boolean boolDictatMode, boolMarkSigns, boolMarkPoints;
    private Map<Float, Float> mpMarkPointList;
    private View view;
    private Mode mode;
    
    /**
     * The default-constructor {@link #clsMarkPointList(int, float, float, boolean) }
     */
    public clsMarkPointList() {
        this(20, 0.1f, 0.5f, false);
    }
    
    public clsMarkPointList(int maxPoints, float markMultiplier, float pointsMultiplier, boolean dictatMode) {
        super();
        this.intMaxPoints = maxPoints;
        this.fltMarkMultiplier = markMultiplier;
        this.fltPointsMultiplier = pointsMultiplier;
        this.boolDictatMode = dictatMode;
        if(this.boolDictatMode) {
            this.fltBestMarkAt = 0.0f;
            this.fltWorstMarkTo = this.intMaxPoints;
        } else {
            this.fltBestMarkAt = this.intMaxPoints;
            this.fltWorstMarkTo = 0.0f;
        }
        this.fltCustomMark = 3.5f;
        this.fltCustomPoints = this.intMaxPoints/2;
        this.view = View.bestMarkFirst;
        this.mode = Mode.linear;
        this.mpMarkPointList = new LinkedHashMap<>();
    }
    
    public int getMaxPoints() {
        return this.intMaxPoints;
    }
    
    public void setMaxPoints(int maxPoints) {
        this.intMaxPoints = maxPoints;
    }
    
    public float getMarkMultiplier() {
        return this.fltMarkMultiplier;
    }
    
    public void setMarkMultiplier(float markMultiplier) {
        this.fltMarkMultiplier = markMultiplier;
    }
    
    public float getPointsMultiplier() {
        return this.fltPointsMultiplier;
    }
    
    public void setPointsMultiplier(float pointsMultiplier) {
        this.fltPointsMultiplier = pointsMultiplier;
    }
    
    public float getBestMarkAt() {
        return this.fltBestMarkAt;
    }
    
    public void setBestMarkAt(float bestMarkAt) {
        this.fltBestMarkAt = bestMarkAt;
    }
    
    public float getWorstMarkTo() {
        return this.fltWorstMarkTo;
    }
    
    public void setWorstMarkTo(float worstMarkTo) {
        this.fltWorstMarkTo = worstMarkTo;
    }
    
    public boolean isInDictatMode() {
        return this.boolDictatMode;
    }
    
    public void setDictatMode(boolean dictatMode) {
        this.boolDictatMode = dictatMode;
    }
    
    public float getCustomPoints() {
        return this.fltCustomPoints;
    }
    
    public void setCustomPoints(float customPoints) {
        this.fltCustomPoints = customPoints;
    }
    
    public float getCustomMark() {
        return this.fltCustomMark;
    }
    
    public void setCustomMark(float customMark) {
        this.fltCustomMark = customMark;
    }
    
    public boolean isMarkSigns() {
        return this.boolMarkSigns;
    }
    
    public void setMarkSigns(boolean markSigns) {
        this.boolMarkSigns = markSigns;
    }
    
    public boolean isMarkPoints() {
        return this.boolMarkPoints;
    }
    
    public void setMarkPoints(boolean markPoints) {
        this.boolMarkPoints = markPoints;
    }
    
    public Map<Float, Float> getMarkPointList() {
        return this.mpMarkPointList;
    }

    public Mode getMode() {
        return this.mode;
    }
    
    public void setMode(Mode mode) {
        this.mode = mode;
    }
    
    public View getView() {
        return this.view;
    }
    
    public void setView(View view) {
        this.view = view;
    }
    
    public Map<Float, Float> generateMarkPointList() {
        mpMarkPointList.clear();
        switch(mode) {
            case linear:
                mpMarkPointList = this.generateLinearList(1.0f, this.intMaxPoints, 6.0f, 0.0f, mpMarkPointList);
                break;
            case exponential:
                this.mpMarkPointList = this.generateExponentialList(mpMarkPointList);
                break;
            case withCrease:
                if(view == View.bestMarkFirst || view == View.worstMarkFirst) {
                    mpMarkPointList.put(6.0f, fltWorstMarkTo);
                    mpMarkPointList = this.generateLinearList(this.fltCustomMark, this.fltCustomPoints, 6.0f, this.fltWorstMarkTo, mpMarkPointList);
                    mpMarkPointList = this.generateLinearList(1.0f, this.fltBestMarkAt, this.fltCustomMark, this.fltCustomPoints, mpMarkPointList);
                    mpMarkPointList.put(1.0f, fltBestMarkAt);
                } else {
                    mpMarkPointList = this.generateLinearList(1.0f, this.intMaxPoints, 1.0f, this.fltBestMarkAt, mpMarkPointList);
                    mpMarkPointList = this.generateLinearList(1.0f, this.fltBestMarkAt, this.fltCustomMark, this.fltCustomPoints, mpMarkPointList);
                    mpMarkPointList = this.generateLinearList(this.fltCustomMark, this.fltCustomPoints, 6.0f, this.fltWorstMarkTo, mpMarkPointList);
                    mpMarkPointList = this.generateLinearList(6.0f, this.fltWorstMarkTo, 6.0f, 0.0f, mpMarkPointList);
                }
                break;
            case ihk:
                mpMarkPointList = this.generateLinearList(1.0f, this.intMaxPoints, 6.0f, 0.0f, mpMarkPointList);
                Map<Float, Float> tmp = new LinkedHashMap<>();
                tmp.putAll(this.mpMarkPointList);
                this.mpMarkPointList.clear();
                Map<Double, Double> IHK = this.getIHKList();
                if(view == View.lowestPointsFirst || view == View.highestPointsFirst) {
                    for(Entry<Float, Float> entry : tmp.entrySet()) {
                        double percentage = round(entry.getKey()/(float)(this.intMaxPoints/100.0),1.0f);
                        double mark = IHK.get(percentage);
                        this.mpMarkPointList.put(entry.getKey(), (float)mark);
                    }
                } else {
                    for(Entry<Float, Float> entry : tmp.entrySet()) {
                        double percentage = round(entry.getValue()/(float)(this.intMaxPoints/100.0),1.0f);
                        double mark = IHK.get(percentage);
                        this.mpMarkPointList.put((float)mark, entry.getValue());
                    }
                }
                break;
            default:
        }
        
        
        if(view == View.lowestPointsFirst) {
            Map<Float, Float> tmp = new LinkedHashMap<>();
            tmp.putAll(this.mpMarkPointList);
            this.mpMarkPointList.clear();
            for(float key : tmp.keySet()) {
                if(tmp.get(round(this.intMaxPoints-key, this.fltPointsMultiplier))==null) {
                    this.mpMarkPointList.put(this.intMaxPoints-key, this.mpMarkPointList.get(key-this.fltPointsMultiplier));
                } else {
                    this.mpMarkPointList.put(this.intMaxPoints-key, tmp.get(round(this.intMaxPoints-key, this.fltPointsMultiplier)));
                }
            }
        } 
        if(view == View.bestMarkFirst) {
            Map<Float, Float> tmp = new LinkedHashMap<>();
            tmp.putAll(this.mpMarkPointList);
            this.mpMarkPointList.clear();
            float old = 1.0f;
            for(float key : tmp.keySet()) {
                if(tmp.get(round((6.0f-key)+1.0f, this.fltMarkMultiplier))==null) {
                    this.mpMarkPointList.put(round(6.0f-key, this.fltMarkMultiplier) + 1.0f, old);
                } else {
                    old = tmp.get(round(((6.0f-key) + 1.0f), this.fltMarkMultiplier));
                    this.mpMarkPointList.put(round(6.0f-key, this.fltMarkMultiplier) + 1.0f, old);
                }
            }
        }
        
        if(this.isMarkPoints()) {
            Map<Float, Float> tmp = new LinkedHashMap<>();
            tmp.putAll(this.mpMarkPointList);
            Map<Double, Double> abitur = this.getAbiturPointList();
            this.mpMarkPointList.clear();
            if(view == View.lowestPointsFirst || view == View.highestPointsFirst) {
                for(Entry<Float, Float> entry : tmp.entrySet()) {
                    double percentage = entry.getKey()/(float)(this.intMaxPoints/100.0);
                    double mark = abitur.get((double)round((float)percentage, 1.0f));
                    this.mpMarkPointList.put(entry.getKey(), (float)mark);
                }
            } else {
                for(Entry<Float, Float> entry : tmp.entrySet()) {
                    double percentage = entry.getValue()/((float)this.intMaxPoints/100.0);
                    double mark = abitur.get((double)round((float)percentage, 1.0f));
                    this.mpMarkPointList.put((float)mark, entry.getValue());
                }
            }
        }
        
        if(this.isInDictatMode()) {
            Map<Float, Float> tmp = new LinkedHashMap<>();
            tmp.putAll(this.mpMarkPointList);
            this.mpMarkPointList.clear();
            if(view == View.lowestPointsFirst || view == View.highestPointsFirst) {
                for(Entry<Float, Float> entry : tmp.entrySet()) {
                    this.mpMarkPointList.put(this.intMaxPoints - entry.getKey(), entry.getValue());
                }
            } else {
                for(Entry<Float, Float> entry : tmp.entrySet()) {
                    this.mpMarkPointList.put(entry.getKey(), this.intMaxPoints - entry.getValue());
                }
            }
        }
        
        return this.mpMarkPointList;
    }
    
    public Map<String, String> getSignedList(Map<Float, Float> list) {
        Map<String, String> signedList = new LinkedHashMap<>();
        if(this.isMarkSigns()) {
            if(this.fltMarkMultiplier==0.25f && !this.isMarkPoints()) {
                Map<Float, Float> tmp = new LinkedHashMap<>();
                tmp.putAll(this.mpMarkPointList);
                this.mpMarkPointList.clear();
                if(view == View.lowestPointsFirst || view == View.highestPointsFirst) {
                    for(Entry<Float, Float> entry : tmp.entrySet()) {
                        String value = entry.getValue().toString();
                        value = value.replace(".25", "-");
                        if(value.endsWith(".75")) {
                            value = String.valueOf(Integer.parseInt(value.replace(".", "-").split("-")[0])+1) + "+";
                        }
                        if(value.endsWith("5")) {
                            value = value.replace(".5", "-" + String.valueOf(Integer.parseInt(value.replace(".", "-").split("-")[0])+1));
                        }
                        value = value.replace(".0", "");
                        signedList.put(entry.getKey().toString(), value);
                    }
                } else {
                    for(Entry<Float, Float> entry : tmp.entrySet()) {
                        String value = entry.getKey().toString();
                        value = value.replace(".25", "-");
                        if(value.endsWith(".75")) {
                            value = String.valueOf(Integer.parseInt(value.replace(".", "-").split("-")[0])+1) + "+";
                        }
                        if(value.endsWith("5")) {
                            value = value.replace(".5","-" + String.valueOf(Integer.parseInt(value.replace(".", "-").split("-")[0])+1));
                        }
                        value = value.replace(".0", "");
                        signedList.put(value, entry.getValue().toString());
                    }
                }
            }
        }
        return signedList;
    }
    
    public float searchPoints(float mark, View tmpview, Map<Float, Float> tmp) {
        float points = 0.0f;
        if(tmpview == View.bestMarkFirst ||tmpview == View.worstMarkFirst) {
            if(tmp.containsKey(mark)) {
                points = tmp.get(mark);
            }
        } else {
            if(tmp.containsValue(mark)) {
               for(Entry<Float, Float> entry : tmp.entrySet()) {
                   if(entry.getValue().equals(mark)) {
                       points = entry.getKey();
                   }
               } 
            }
        }
        return points;
    }
    
    public float searchMark(float points, View tmpview, Map<Float, Float> tmp) {
        float mark = 0.0f;
        if(tmpview == View.bestMarkFirst ||tmpview == View.worstMarkFirst) {
            if(tmp.containsValue(points)) {
               for(Entry<Float, Float> entry : tmp.entrySet()) {
                   if(entry.getValue().equals(points)) {
                       mark = entry.getKey();
                   }
               } 
            }
        } else {
            if(tmp.containsKey(points)) {
                mark = tmp.get(points);
            }
        }
        return mark;
    }
    
    /**
     * Generates a line between two points<br>
     * see this function:<br>
     *  y=((y2-y1)/(x2-x1))x + ((x2y1-x1y2)/(x2-x1))
     *
     * @param startMark
     * @param startPoints
     * @param endMark
     * @param endPoints
     * @param tmp
     * @return 
     */
    private Map<Float, Float> generateLinearList(float startMark, float startPoints, float endMark, float endPoints, Map<Float, Float> tmp) {
        if(tmp==null) {
            tmp = new LinkedHashMap<>();
        }
        float diffMark = endMark - startMark;
        float diffPoints = startPoints - endPoints;
        switch(this.view) {
            case bestMarkFirst:
            case worstMarkFirst:
                for(float currentMark = endMark; currentMark>= startMark; currentMark -= this.fltMarkMultiplier) {
                    float result = ((currentMark-startMark)/(diffMark/diffPoints)) + endPoints;
                    float tmpResult =  (startPoints - result) + endPoints;
                    if(!tmp.containsKey(round(currentMark, this.fltMarkMultiplier))) {
                        tmp.put(round(currentMark, this.fltMarkMultiplier), round(tmpResult, this.fltPointsMultiplier));
                    }
                }
                break;
            case highestPointsFirst:
            case lowestPointsFirst:
                for(float currentPoints = startPoints; currentPoints>= endPoints; currentPoints -= this.fltPointsMultiplier) {
                    float result = (diffMark/diffPoints)*(currentPoints - endPoints) + startMark;
                    float tmpResult = (endMark - result) + startMark;
                    tmp.put(round(currentPoints, this.fltPointsMultiplier), round(tmpResult, this.fltMarkMultiplier));
                }
                break;
        }
        return tmp;
    }
    
    private Map<Float,Float> generateExponentialList(Map<Float, Float> tmp) {
        if(tmp==null) {
            tmp = new LinkedHashMap<>();
        }
        Map<Float, Float> mp = new LinkedHashMap<>();
        double sum = 0.0;
        switch(this.view) {
            case bestMarkFirst:
            case worstMarkFirst:
                clsMarkPointList list = new clsMarkPointList();
                list.setMaxPoints(this.intMaxPoints);
                list.setPointsMultiplier(0.01f);
                list.setMarkMultiplier(0.1f);
                
                list.setDictatMode(false);
                list.setView(View.lowestPointsFirst);
                list.setMode(Mode.exponential);
                mp = list.generateMarkPointList();
                mp = reverse(mp);
                for(Entry<Float, Float> entry : mp.entrySet()) {
                    tmp.put(round(entry.getKey(), this.fltMarkMultiplier), round(entry.getValue(), this.fltPointsMultiplier));
                }
                break;
            case highestPointsFirst:
            case lowestPointsFirst:
                for(float currentPoints = this.intMaxPoints; currentPoints>= 0.0f; currentPoints -= this.fltPointsMultiplier) {
                    float counter = 1/(float)Math.sqrt((double)(2 * Math.PI * ((this.intMaxPoints/2)*(this.intMaxPoints/2))));
                    float taker = ((currentPoints - (this.intMaxPoints/2)) * (currentPoints - (this.intMaxPoints/2))) / (2 * ((this.intMaxPoints) * (this.intMaxPoints)));
                    sum += Math.pow(counter, taker);
                    mp.put(currentPoints, (float)Math.pow(counter, taker));
                }
                double part = sum / 100;
                double old = 1.0;
                for(Entry<Float, Float> entry : mp.entrySet()) {
                    old += ((double)5/100.0)*(entry.getValue()/part);
                    tmp.put(round(entry.getKey(), this.fltPointsMultiplier), round((float)old, this.fltMarkMultiplier));
                }
        }
        return tmp;
    }
    
    private static <K,V> Map<V,K> reverse(Map<K,V> map) {
        Map<V,K> rev = new LinkedHashMap<>();
        for(Entry<K,V> entry : map.entrySet())
        rev.put(entry.getValue(), entry.getKey());
        return rev;
    }
    
    private float round(float value, float multi) {
        DecimalFormat dt = new DecimalFormat("0.00");
        String strValue = dt.format(value).replace(",", ".");
        if(strValue.endsWith("5")) {
            value = Float.parseFloat(strValue.substring(0, strValue.length()-1));
        } else {
            value = Float.parseFloat(strValue.substring(0, strValue.length()));
        }     
        return (float)Math.round(value*(1f/multi))/(1f/multi);
    }
    
    /**
     * Function to validate the settings setted by the Setters<br>
     *  -   The maximum number of points are 9999<br>
     *  -   The Multipliers should be between 0 and 1<br>
     *  -   The bestMarkAt and worstMarkTo should be <br>
     *          - between the maximum number of points and 0<br>
     *          - dependent on the dictatMode 2 points away from themselves<br>
     *  -   The customPoints should be between the bestMarkAt and worstMarkTo<br>
     *  -   The customMark should be between 1.0 and 6.0<br>
     * @return the State of Validation
     */
    public boolean validateSettings() {
        
        // The maximum number of points are 9999
        if(this.intMaxPoints>10000) {
            return false;
        }
        
        // The Multipliers should be between 0 and 1
        if(!(this.fltMarkMultiplier>0.0&&this.fltMarkMultiplier<=1.0)) {
            return false;
        }
        
        if(!(this.fltPointsMultiplier>0.0&&this.fltPointsMultiplier<=1.0)) {
            return false;
        }
        
        /*
            -   The bestMarkAt and worstMarkTo should be 
                - between the maximum number of points and 0
                - dependent on the dictatMode 2 points away from themselves
        */ 
        if(this.isInDictatMode()) {
            if(this.fltWorstMarkTo<this.fltBestMarkAt+2) {
                return false;
            }
            if(this.fltWorstMarkTo>this.intMaxPoints) {
                return false;
            }
            if(this.fltBestMarkAt<0.0) {
                return false;
            }
        } else {
            if(this.fltBestMarkAt<this.fltWorstMarkTo+2) {
                return false;
            }
            if(this.fltBestMarkAt>this.intMaxPoints) {
                return false;
            }
            if(this.fltWorstMarkTo<0.0) {
                return false;
            }
        }
        
        // The customPoints should be between the bestMarkAt and worstMarkTo
        if(this.isInDictatMode()) {
            if(this.fltCustomPoints>=this.fltWorstMarkTo||this.fltCustomPoints<=this.fltBestMarkAt) {
                return false;
            }
        } else {
            if(this.fltCustomPoints>=this.fltBestMarkAt||this.fltCustomPoints<=this.fltWorstMarkTo) {
                return false;
            }
        }
        
        // The customMark should be between 1.0 and 6.0
        return !(this.fltCustomMark>6.0 || this.fltCustomMark<1.0);
    }
    
    /**
     * Enum of the View-Settings
     * bestMarkFirst and worstMarkFirst: 
     *  Marks as Keys and Points as Values
     * highestPointsFirst and lowestPointsFirst: 
     *  Points as Keys and Marks as Values
     */
    public enum View {
        bestMarkFirst,
        worstMarkFirst,
        highestPointsFirst,
        lowestPointsFirst
    }
    
    /**
     * Enum of the Mode-Settings
     * linear: linear MarkPointsList
     * exponential: exponential MarkPointList
     * withCrease: you can choose a custom Point
     * ihk: markPointList with the ihk-MarkList
     */
    public enum Mode {
        linear,
        exponential,
        withCrease,
        ihk
    }
    
    private Map<Double, Double> getIHKList() {
        Map<Double, Double> mapIHK = new LinkedHashMap<>();
        mapIHK.put(100.0, 1.0);
        mapIHK.put(99.0, 1.1);
        mapIHK.put(98.0, 1.1);
        mapIHK.put(97.0, 1.2);
        mapIHK.put(96.0, 1.2);
        mapIHK.put(95.0, 1.3);
        mapIHK.put(94.0, 1.3);
        mapIHK.put(93.0, 1.4);
        mapIHK.put(92.0, 1.4);
        mapIHK.put(91.0, 1.5);
        mapIHK.put(90.0, 1.6);
        mapIHK.put(89.0, 1.7);
        mapIHK.put(88.0, 1.8);
        mapIHK.put(87.0, 1.9);
        mapIHK.put(86.0, 2.0);
        mapIHK.put(85.0, 2.0);
        mapIHK.put(84.0, 2.1);
        mapIHK.put(83.0, 2.2);
        mapIHK.put(82.0, 2.3);
        mapIHK.put(81.0, 2.4);
        mapIHK.put(80.0, 2.5);
        mapIHK.put(79.0, 2.6);
        mapIHK.put(78.0, 2.7);
        mapIHK.put(77.0, 2.7);
        mapIHK.put(76.0, 2.8);
        mapIHK.put(75.0, 2.9);
        mapIHK.put(74.0, 2.9);
        mapIHK.put(73.0, 3.0);
        mapIHK.put(72.0, 3.1);
        mapIHK.put(71.0, 3.1);
        mapIHK.put(70.0, 3.2);
        mapIHK.put(69.0, 3.3);
        mapIHK.put(68.0, 3.3);
        mapIHK.put(67.0, 3.4);
        mapIHK.put(66.0, 3.5);
        mapIHK.put(65.0, 3.6);
        mapIHK.put(64.0, 3.6);
        mapIHK.put(63.0, 3.7);
        mapIHK.put(62.0, 3.7);
        mapIHK.put(61.0, 3.8);
        mapIHK.put(60.0, 3.9);
        mapIHK.put(59.0, 3.9);
        mapIHK.put(58.0, 4.0);
        mapIHK.put(57.0, 4.0);
        mapIHK.put(56.0, 4.1);
        mapIHK.put(55.0, 4.1);
        mapIHK.put(54.0, 4.2);
        mapIHK.put(53.0, 4.3);
        mapIHK.put(52.0, 4.3);
        mapIHK.put(51.0, 4.4);
        mapIHK.put(50.0, 4.4);
        mapIHK.put(49.0, 4.5);
        mapIHK.put(48.0, 4.6);
        mapIHK.put(47.0, 4.6);
        mapIHK.put(46.0, 4.7);
        mapIHK.put(45.0, 4.7);
        mapIHK.put(44.0, 4.8);
        mapIHK.put(43.0, 4.8);
        mapIHK.put(42.0, 4.9);
        mapIHK.put(41.0, 4.9);
        mapIHK.put(40.0, 5.0);
        mapIHK.put(39.0, 5.0);
        mapIHK.put(38.0, 5.0);
        mapIHK.put(37.0, 5.1);
        mapIHK.put(36.0, 5.1);
        mapIHK.put(35.0, 5.2);
        mapIHK.put(34.0, 5.2);
        mapIHK.put(33.0, 5.3);
        mapIHK.put(32.0, 5.3);
        mapIHK.put(31.0, 5.4);
        mapIHK.put(30.0, 5.4);
        mapIHK.put(29.0, 5.5);
        mapIHK.put(28.0, 5.6);
        mapIHK.put(27.0, 5.6);
        mapIHK.put(26.0, 5.6);
        mapIHK.put(25.0, 5.6);
        mapIHK.put(24.0, 5.6);
        mapIHK.put(23.0, 5.6);
        mapIHK.put(22.0, 5.7);
        mapIHK.put(21.0, 5.7);
        mapIHK.put(20.0, 5.7);
        mapIHK.put(19.0, 5.7);
        mapIHK.put(18.0, 5.7);
        mapIHK.put(17.0, 5.7);
        mapIHK.put(16.0, 5.8);
        mapIHK.put(15.0, 5.8);
        mapIHK.put(14.0, 5.8);
        mapIHK.put(13.0, 5.8);
        mapIHK.put(12.0, 5.8);
        mapIHK.put(11.0, 5.9);
        mapIHK.put(10.0, 5.9);
        mapIHK.put(9.0, 5.9);
        mapIHK.put(8.0, 5.9);
        mapIHK.put(7.0, 5.9);
        mapIHK.put(6.0, 5.9);
        mapIHK.put(5.0, 6.0);
        mapIHK.put(4.0, 6.0);
        mapIHK.put(3.0, 6.0);
        mapIHK.put(2.0, 6.0);
        mapIHK.put(1.0, 6.0);
        mapIHK.put(0.0, 6.0);
        return mapIHK;
    }
    
    private Map<Double, Double> getAbiturPointList() {
    Map<Double, Double> mapAbitur = new LinkedHashMap<>();
        mapAbitur.put(100.0, 15.0);
        mapAbitur.put(99.0, 15.0);
        mapAbitur.put(98.0, 15.0);
        mapAbitur.put(97.0, 15.0);
        mapAbitur.put(96.0, 15.0);
        mapAbitur.put(95.0, 15.0);
        mapAbitur.put(94.0, 14.0);
        mapAbitur.put(93.0, 14.0);
        mapAbitur.put(92.0, 14.0);
        mapAbitur.put(91.0, 14.0);
        mapAbitur.put(90.0, 14.0);
        mapAbitur.put(89.0, 13.0);
        mapAbitur.put(88.0, 13.0);
        mapAbitur.put(87.0, 13.0);
        mapAbitur.put(86.0, 13.0);
        mapAbitur.put(85.0, 13.0);
        mapAbitur.put(84.0, 12.0);
        mapAbitur.put(83.0, 12.0);
        mapAbitur.put(82.0, 12.0);
        mapAbitur.put(81.0, 12.0);
        mapAbitur.put(80.0, 12.0);
        mapAbitur.put(79.0, 11.0);
        mapAbitur.put(78.0, 11.0);
        mapAbitur.put(77.0, 11.0);
        mapAbitur.put(76.0, 11.0);
        mapAbitur.put(75.0, 11.0);
        mapAbitur.put(74.0, 10.0);
        mapAbitur.put(73.0, 10.0);
        mapAbitur.put(72.0, 10.0);
        mapAbitur.put(71.0, 10.0);
        mapAbitur.put(70.0, 10.0);
        mapAbitur.put(69.0, 9.0);
        mapAbitur.put(68.0, 9.0);
        mapAbitur.put(67.0, 9.0);
        mapAbitur.put(66.0, 9.0);
        mapAbitur.put(65.0, 9.0);
        mapAbitur.put(64.0, 8.0);
        mapAbitur.put(63.0, 8.0);
        mapAbitur.put(62.0, 8.0);
        mapAbitur.put(61.0, 8.0);
        mapAbitur.put(60.0, 8.0);
        mapAbitur.put(59.0, 7.0);
        mapAbitur.put(58.0, 7.0);
        mapAbitur.put(57.0, 7.0);
        mapAbitur.put(56.0, 7.0);
        mapAbitur.put(55.0, 7.0);
        mapAbitur.put(54.0, 6.0);
        mapAbitur.put(53.0, 6.0);
        mapAbitur.put(52.0, 6.0);
        mapAbitur.put(51.0, 6.0);
        mapAbitur.put(50.0, 6.0);
        mapAbitur.put(49.0, 5.0);
        mapAbitur.put(48.0, 5.0);
        mapAbitur.put(47.0, 5.0);
        mapAbitur.put(46.0, 5.0);
        mapAbitur.put(45.0, 5.0);
        mapAbitur.put(44.0, 4.0);
        mapAbitur.put(43.0, 4.0);
        mapAbitur.put(42.0, 4.0);
        mapAbitur.put(41.0, 4.0);
        mapAbitur.put(40.0, 4.0);
        mapAbitur.put(39.0, 4.0);
        mapAbitur.put(38.0, 3.0);
        mapAbitur.put(37.0, 3.0);
        mapAbitur.put(36.0, 3.0);
        mapAbitur.put(35.0, 3.0);
        mapAbitur.put(34.0, 3.0);
        mapAbitur.put(33.0, 3.0);
        mapAbitur.put(32.0, 2.0);
        mapAbitur.put(31.0, 2.0);
        mapAbitur.put(30.0, 2.0);
        mapAbitur.put(29.0, 2.0);
        mapAbitur.put(28.0, 2.0);
        mapAbitur.put(27.0, 2.0);
        mapAbitur.put(26.0, 1.0);
        mapAbitur.put(25.0, 1.0);
        mapAbitur.put(24.0, 1.0);
        mapAbitur.put(23.0, 1.0);
        mapAbitur.put(22.0, 1.0);
        mapAbitur.put(21.0, 1.0);
        mapAbitur.put(20.0, 1.0);
        mapAbitur.put(19.0, 0.0);
        mapAbitur.put(18.0, 0.0);
        mapAbitur.put(17.0, 0.0);
        mapAbitur.put(16.0, 0.0);
        mapAbitur.put(15.0, 0.0);
        mapAbitur.put(14.0, 0.0);
        mapAbitur.put(13.0, 0.0);
        mapAbitur.put(12.0, 0.0);
        mapAbitur.put(11.0, 0.0);
        mapAbitur.put(10.0, 0.0);
        mapAbitur.put(9.0, 0.0);
        mapAbitur.put(8.0, 0.0);
        mapAbitur.put(7.0, 0.0);
        mapAbitur.put(6.0, 0.0);
        mapAbitur.put(5.0, 0.0);
        mapAbitur.put(4.0, 0.0);
        mapAbitur.put(3.0, 0.0);
        mapAbitur.put(2.0, 0.0);
        mapAbitur.put(1.0, 0.0);
        mapAbitur.put(0.0, 0.0);
        return mapAbitur;
    }
}