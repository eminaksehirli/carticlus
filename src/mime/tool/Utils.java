/* An utility class.
 *  
 * Copyright (C) 2011 - 2013  Emin Aksehirli, Sandy Moens
 *
 * This file is part of MIME Framework - http://adrem.ua.ac.be/mime . 
 * 
 * MIME is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package mime.tool;

import static java.util.Arrays.fill;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility functions
 * 
 * @author Sandy Moens
 * @author Emin Aksehirli
 */
public class Utils
{
	/**
	 * Computes the factorial of a positive integer
	 * 
	 * @param num
	 *          number of which the factorial is to be computed
	 * @return the factorial
	 */
	public static int factorial(int num)
	{
		if (num <= 1)
		{
			return 1;
		}
		int result = 1;
		for (int i = 2; i <= num; i++)
		{
			result *= i;
		}
		return result;
	}

	/**
	 * Computes the sum of a positive integer
	 * 
	 * @param num
	 *          number of which the sum is to be computed
	 * @return the sum
	 */
	public static int sum(int num)
	{
		if (num <= 1)
		{
			return 1;
		}
		return num * (num + 1) / 2;
	}

	/**
	 * Computes the combination of bottom items out of top choices
	 * 
	 * @param top
	 *          the number of items from which can be chosen
	 * @param bottom
	 *          the number of items that need to be chosen
	 * @return the number of possible combinations with given top and bottom value
	 */
	public static double combination(double top, double bottom)
	{
		// if (bottom > top) {
		// return 0;
		// } else {
		// double s = 1;
		// for (double d = top; d > 0; d--) {
		// s *= d;
		// System.out.println("s " + s);
		// }
		// System.out.println(Utils.factorial(top));
		// System.out.println(Utils.factorial(bottom));
		// System.out.println(Utils.factorial(top - top));
		// return Utils.factorial(top)
		// / (Utils.factorial(bottom) * Utils.factorial(top - bottom));
		// }
		if (bottom > top)
			return 0;

		double r = (bottom > top - bottom ? bottom : top - bottom) + 1;
		if (bottom == top)
			return 1;

		double d = 2;
		for (double m = r + 1; m <= top; m++, d++)
		{
			r *= m;
			r /= d;
		}
		return r;
	}

	public static double combinationWithRepetition(double top, double bottom)
	{
		return combination(top + bottom - 1, bottom);
	}

	public static int countNumberOfLines(String fileName)
	{
		int size = 0;
		LineNumberReader lnr;
		try
		{
			lnr = new LineNumberReader(new FileReader(fileName));
			while (lnr.readLine() != null)
			{
			}
			size = lnr.getLineNumber();
			lnr.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return size;
	}

	public static int logarithmicIndexSearch(double[] values, double toSearch)
	{
		double left = 0;
		double right = values.length;
		double split;

		if (right % 2 == 0)
		{
			while (right - left > 3)
			{
				split = (left + (right - left) / 2.0);
				if (values[(int) split] >= toSearch)
				{
					right = split;
				} else
				{
					left = split;
				}
			}
			for (int i = 0; i < 3; i++)
			{
				if (values[(int) left + i] >= toSearch)
				{
					right = left + i;
					break;
				}
			}
		} else
		{
			while (right - left > 1)
			{
				split = (left + (right - left) / 2.0);
				if (values[(int) split] >= toSearch)
				{
					right = split;
				} else
				{
					left = split;
				}
			}
		}
		return (int) (right);
	}

	public static double log(double x, double base)
	{
		return Math.log(x) / Math.log(base);
	}

	public static boolean checkFileExistance(String filePath)
	{
		return new File(filePath).exists();
	}

	/**
	 * Returns a range of int's between start (inclusive) and end (exclusive).
	 * 
	 * @param start
	 *          Starting number (inclusive)
	 * @param end
	 *          Ending number (exclusive)
	 * @param increment
	 *          Step size
	 * @return int array containing numbers between start and end incremented by
	 *         increment
	 */
	public static int[] range(int start, int end, int increment)
	{
		int[] range = new int[(int) Math.ceil((end - start) / (double) increment)];

		int currentValue = start;
		for (int i = 0; i < range.length; i++)
		{
			range[i] = currentValue;
			currentValue += increment;
		}

		return range;
	}

	/**
	 * Returns a range of int's between start (inclusive) and end (exclusive).
	 * 
	 * @param start
	 *          Starting number (inclusive)
	 * @param end
	 *          Ending number (exclusive)
	 * @return int array containing numbers between start and end incremented by 1
	 */
	public static int[] range(int start, int end)
	{
		return range(start, end, 1);
	}

	public static int sum(Collection<Integer> numbers)
	{
		int sum = 0;
		for (int i : numbers)
		{
			sum += i;
		}
		return sum;
	}

	/**
	 * Gives equal sized partitions for n. If n is not dividable by p, the last
	 * partition is smaller. Useful method for partitioning for Multi-processing.
	 * 
	 * @param n
	 *          Number to partition.
	 * @param p
	 *          Number of partitions.
	 * @return An p-sized int array that has the partition sizes.
	 */
	public static int[] partition(int n, int p)
	{
		final int shareSize = n / p;
		final int leftOver = n - (p - 1) * shareSize;

		final int[] shareSizes = new int[p];
		fill(shareSizes, shareSize);
		shareSizes[shareSizes.length - 1] = leftOver;
		return shareSizes;
	}

	public static List<String> convertToStringsList(List<?> list)
	{
		List<String> stringsList = new LinkedList<String>();
		for (Iterator<?> it = list.iterator(); it.hasNext();)
		{
			stringsList.add(it.next().toString());
		}
		return stringsList;
	}

	public static double[] range(double start, double end, double increment)
	{
		double[] range = new double[(int) Math.ceil((end - start) / increment)];

		double currentValue = start;
		for (int i = 0; i < range.length; i++)
		{
			range[i] = currentValue;
			currentValue += increment;
		}

		return range;
	}
}
