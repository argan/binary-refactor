package org.hydra.util;

import java.util.ArrayList;
import java.util.List;

public class Lists {
	public static interface MapFunc<In, Out> {
		public Out apply(In in);
	}

	public static interface AccFunc<In, Acc> {
		public Acc apply(In in, Acc out);
	}

	public static interface MapAccFunc<In, Out, Acc> {
		public Pair<Out, Acc> apply(In in, Acc acc);
	}

	public static interface Predicate<In> {
		public boolean apply(In in);
	}

	public static class Pair<Left, Right> {
		private Left left;
		private Right right;

		public Pair(Left left, Right right) {
			this.left = left;
			this.right = right;
		}

		public Left getLeft() {
			return left;
		}

		public Right getRight() {
			return right;
		}

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((left == null) ? 0 : left.hashCode());
            result = prime * result + ((right == null) ? 0 : right.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Pair other = (Pair) obj;
            if (left == null) {
                if (other.left != null)
                    return false;
            } else if (!left.equals(other.left))
                return false;
            if (right == null) {
                if (other.right != null)
                    return false;
            } else if (!right.equals(other.right))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Pair [left=" + left + ", right=" + right + "]";
        }

	}

	/**
	 * map function 
	 * 
	 * @param <In>
	 * @param <Out>
	 * @param in
	 * @param f
	 * @return
	 */
	public static <In, Out> List<Out> map(List<In> in, MapFunc<In, Out> f) {
		List<Out> out = new ArrayList<Out>(in.size());
		for (In inObj : in) {
			out.add(f.apply(inObj));
		}
		return out;
	}

	/**
	 * filter function
	 * 
	 * @param <In>
	 * @param in
	 * @param f
	 * @return
	 */
	public static <In> List<In> filter(List<In> in, Predicate<In> f) {
		List<In> out = new ArrayList<In>();
		for (In inObj : in) {
			if (f.apply(inObj)) {
				out.add(inObj);
			}
		}
		return out;
	}

	public static <In, Acc> Acc acc(List<In> in, Acc init, AccFunc<In, Acc> f) {
		Acc out = init;
		for (In inObj : in) {
			out = f.apply(inObj, out);
		}
		return out;
	}

	public static <In, Out, Acc> Pair<List<Out>, Acc> mapAndAcc(List<In> in, Acc init, MapAccFunc<In, Out, Acc> f) {
		Acc out = init;
		List<Out> list = new ArrayList<Out>(in.size());
		for (In inObj : in) {
			Pair<Out, Acc> pair = f.apply(inObj, out);
			out = pair.getRight();
			list.add(pair.getLeft());
		}
		return new Pair<List<Out>, Acc>(list, out);
	}

}
