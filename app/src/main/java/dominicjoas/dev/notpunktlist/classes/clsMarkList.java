package dominicjoas.dev.notpunktlist.classes;

import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;

/**
 * @author dj
 * @version 1.0
 */
public class clsMarkList {
    private final List<Double> lsMarks;
    private final List<Double> lsPoints;
    private DecimalFormat df =   new DecimalFormat  ( "00.00" );
    private final boolean boolWholePoints, boolTenthMarks;
    private final double dblMaxPoints;

    /**
     * Basic-Constructor
     * @param maxPoints
     * maximum Points in a Class-Test
     * @param wholePoints
     * whole Points or half Points
     * @param tenthMarks
     * tenth Marks or quarter Marks
     */
    public clsMarkList(double maxPoints, boolean wholePoints, boolean tenthMarks) {
        this.lsPoints = new ArrayList<>();
        this.lsMarks = new ArrayList<>();

        this.boolWholePoints = wholePoints;
        this.boolTenthMarks = tenthMarks;

        this.dblMaxPoints = maxPoints;
    }

    /**
     * Create mark-list
     * @return
     * return List
     */
    public List<String> generateList() {
        List<String> lsPointMarks = new ArrayList<>();
        double mark;
        this.createLists();
        for(double point : this.lsPoints) {
            mark = this.calculateMarks(point);
            if(this.boolTenthMarks) {
                int whole = (int) mark;
                DecimalFormat dt = new DecimalFormat("0.00");
                double dblMark = Double.parseDouble(dt.format(mark).replace(",", "."));
                if(mark==whole+0.25 || mark==whole+0.75) {
                    mark = dblMark+0.05;
                }
            }
            mark = Double.parseDouble(df.format(mark).replace(",", "."));
            lsPointMarks.add(point + ";" + df.format(this.closest(mark, this.lsMarks)).replace(",", "."));
        }
        return lsPointMarks;
    }

    /**
     * Find the Mark of Points
     * @param ls
     * String-Mark-List
     * @param points
     * Mark
     * @return
     * return Marks
     */
    public double findMark(List<String> ls, double points) {
        double mark = 0.0;
        for(String strMark : ls) {
            if(points==Double.parseDouble(strMark.split(";")[0])) {
                mark = Double.parseDouble(strMark.split(";")[1]);
                return mark;
            }
        }
        return calculateMarks(points);
    }

    /**
     * Find the Points of a Mark
     * @param ls
     * String-Mark-List
     * @param mark
     * Mark
     * @return
     * return points
     */
    public double findPoints(List<String> ls, double mark) {
        double points = 0.0;
        for(String strMark : ls) {
            if(mark==Double.parseDouble(strMark.split(";")[1])) {
                points = Double.parseDouble(strMark.split(";")[0]);
                return points;
            }
        }
        return calculatePoints(mark);
    }


    public double closestInString(double searchedMark, List<String> in) {
        double min = Double.MAX_VALUE;
        double closest = searchedMark;
        for (String actMark : in) {
            double dblMark = Double.parseDouble(actMark);
            if(searchedMark>dblMark) {
                if(min>(searchedMark-dblMark)) {
                    min = searchedMark - dblMark;
                    closest = dblMark;
                }
            } else {
                if(min>(dblMark-searchedMark)) {
                    min = dblMark - searchedMark;
                    closest = dblMark;
                }
            }
        }

        return closest;
    }

    /**
     * Get the closest Mark in the List
     * @param searchedMark
     * Mark to Search
     * @param in
     * List of Marks
     * @return
     * Closest Mark in the List
     */
    public double closest(double searchedMark, List<Double> in) {
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

    /**
     * Calculate Mark: linear
     * @param actPoints
     * Points
     * @return
     * mark
     */
    private double calculateMarks(double actPoints) {
        double actMark = actPoints * 5;
        actMark = actMark / this.dblMaxPoints;
        actMark = actMark - 6;
        actMark = actMark * -1;
        return actMark;
    }

    private double calculatePoints(double actMark) {
        double actPoints = -actMark + 6;
        actPoints = actPoints * this.dblMaxPoints;
        return actPoints / 5;
    }

    /**
     * Create Point- and Mark-List
     */
    private void createLists() {
        this.lsMarks.clear();
        this.lsPoints.clear();
        double counter;
        if(this.boolTenthMarks) {
            counter = 0.1;
            df = new DecimalFormat("0.0");
        } else {
            counter = 0.25;
            df = new DecimalFormat("0.00");
        }

        for(double i = 1.0;i<=6;i=i+counter) {
            this.lsMarks.add(i);
        }

        if(this.boolWholePoints) {
            counter = 1.00;
        } else {
            counter = 0.5;
        }

        for(double i = 0.0;i<=this.dblMaxPoints;i=i+counter) {
            this.lsPoints.add(i);
        }
    }
}