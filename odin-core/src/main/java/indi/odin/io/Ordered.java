package indi.odin.io;

import java.util.Comparator;

/**
 * 排序支持
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public interface Ordered {

    default int getOrder() {return 0;}

    class OrderComparator implements Comparator<Ordered> {

        static OrderComparator INSTANCE = new OrderComparator();

        @Override
        public int compare(Ordered o1, Ordered o2) {
            return Integer.compare(o2.getOrder(), o1.getOrder());
        }
    }
}
