
package com.phm.comp.filter;

import com.phm.annotations.PublicationDetails;
import com.phm.annotations.PublicationType;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

@PublicationDetails (
    author = {"Xiaobai Li", "Jie Chen", "Guoying Zhao", "Matti Pietikainen"},
        title =  "Remote Heart Rate Measurement From Face Videos Under Realistic Situations",
        journal = "2014 IEEE Conference Computer Vision and Pattern Recognition (CVPR)",
        type = PublicationType.Proceedings,
        year = "2014",
        pages = "4264 - 4271",
        publisher = "IEEE",
        customData = {"Heart rate is an important indicator of people's physiological state. "
                + "    Recently, several papers reported methods to measure heart rate remotely"
                + "    from face videos. Those methods work well on stationary subjects under "
                + "    well controlled conditions, but their performance significantly degrades"
                + "    if the videos are recorded under more challenging conditions, specifically"
                + "    when subjects' motions and illumination variations are involved. We propose "
                + "    a framework which utilizes face tracking and Normalized Least Mean Square "
                + "    adaptive filtering methods to counter their influences. We test our framework"
                + "    on a large difficult and public database MAHNOB-HCI and demonstrate that our "
                + "    method substantially outperforms all previous methods. We also use our method"
                + "    for long term heart rate monitoring in a game evaluation scenario and achieve promising results."}
)
public class NonRigidVariationFilter implements VectorFilter {

    protected int numSegment = 14;
    protected float percentEliminate = 0.05f;
    
    public NonRigidVariationFilter () {
        // Empty body
    }
    public NonRigidVariationFilter (int ns) {
        numSegment = ns;
    }
    public NonRigidVariationFilter (int ns, float pe) {
        this (ns);
        percentEliminate = pe;
    }
    
    public void setEliminationPercent (float pe) {
        percentEliminate = pe;
    }
    public float getEliminationPercent () {
        return percentEliminate;
    }
    public void setNumberOfSegment (int ns) {
        numSegment = ns;
    }
    public int getNumberOfSegment () {
        return numSegment;
    }
    
    @Override
    public RealVector filter(RealVector data) {
        if (data.getDimension() < numSegment) {
            throw new InvalidParameterException("The number of segments should "
                    + "be lower that the dimension of the signal.");
        }
        int segsize = (int) (data.getDimension() * (1.0f / (float) numSegment));
        StandardDeviation std = new StandardDeviation();
        ArrayList<SegElement> segSTD = new ArrayList<>(numSegment);
        for (int index = 0; index < numSegment; index++) {
            int begin = index * segsize;
//            int end = index * (segsize + 1);
//            end = end < data.getDimension() ? end : data.getDimension();
            int tmpsize = segsize > (data.getDimension() - begin) ? 
                    (data.getDimension() - begin) : segsize;
            segSTD.add(index, new SegElement(index, 
                    std.evaluate(data.toArray(), begin, tmpsize)));
        }
        
        Collections.sort(segSTD, new SegElementSTDSort());
        int elen = (int) Math.ceil(segSTD.size() * percentEliminate);
        for (int index = elen; index > 0; index--) {
            segSTD.remove(0);
        }
        Collections.sort(segSTD, new SegElementIndexSort());
        RealVector res = new ArrayRealVector();
        for (int index = 0; index < segSTD.size(); index++) {
            int begin = segSTD.get(index).index * segsize;
            RealVector tmp = data.getSubVector(begin, segsize);
            res = res.append(tmp);
        }
        return res;
    }
    
    class SegElement implements Comparable<SegElement> {
        public int index;
        public double std;
        
        public SegElement (int i, double s) {
            index = i;
            std = s;
        }

        @Override
        public int compareTo(SegElement o) {
            if (this.std < o.std) {
                return -1;
            } if (this.std > o.std) {
                return 1;
            }
            
            return 0;
        }
    }
    public class SegElementSTDSort implements Comparator<SegElement> {
        @Override
        public int compare(SegElement o1, SegElement o2) {
            if (o1.std < o2.std) {
                return -1;
            } if (o1.std > o2.std) {
                return 1;
            }

            return 0;
        }
    }
    public class SegElementIndexSort implements Comparator<SegElement> {
        @Override
        public int compare(SegElement o1, SegElement o2) {
            if (o1.index < o2.index) {
                return -1;
            } if (o1.index > o2.index) {
                return 1;
            }

            return 0;
        }
    }
}
