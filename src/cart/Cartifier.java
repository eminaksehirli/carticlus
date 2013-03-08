package cart;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Math.abs;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class Cartifier
{

	static final int BufferSize = 262144;
	protected List<List<Double>> db;
	protected List<ArrayList<Pair>> carts;

	public Cartifier(List<List<Double>> db)
	{
		this.db = db;
	}

	public void cartifyNumeric(int[] dimensions, String filename)
	{

		int numOfItems = db.size();
		this.carts = new ArrayList<ArrayList<Pair>>(numOfItems * dimensions.length);
		PrintStream p = null;

		if (filename != null)
		{
			try
			{
				p = new PrintStream(new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(filename
						+ ".part")),
						BufferSize));
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		if (dimensions.length == 1)
		{
			fastCartifier(dimensions, numOfItems, p);
		} else
		{
			for (int i = 0; i < numOfItems; i++)
			{
				List<Double> object_i = db.get(i);
				ArrayList<Pair> cart = new ArrayList<Pair>(numOfItems);
				for (int j = 0; j < numOfItems; j++)
				{
					List<Double> object_j = db.get(j);
					double distance;
					if (dimensions.length == 1)
					{
						int d = dimensions[0];
						distance = Math.abs(object_i.get(d) - object_j.get(d));
					} else
					{
						double sum = 0;
						for (int d : dimensions)
						{
							sum += Math.pow(object_i.get(d) - object_j.get(d), 2);
						}
						distance = Math.sqrt(sum);
					}
					cart.add(new Pair(j, distance));
				}

				Collections.sort(cart);

				if (p != null)
				{
					writeCartToFile(cart, p, false);
				} else
				{
					carts.add(cart);
				}
			}
		}
		if (p != null)
		{
			p.close();
			File f = new File(filename + ".part");
			f.renameTo(new File(filename));
		}
	}

	private void fastCartifier(int[] dimensions, int numOfItems, PrintStream p)
	{
		final int d = dimensions[0];

		Pair[] projection = new Pair[numOfItems];
		for (int i = 0; i < numOfItems; i++)
		{
			projection[i] = new Pair(i, db.get(i).get(d));
		}

		Arrays.sort(projection);

		int[] positionOf = new int[projection.length];
		for (int i = 0; i < projection.length; i++)
		{
			positionOf[projection[i].getFirst()] = i;
		}

		for (int itemIx = 0; itemIx < positionOf.length; itemIx++)
		{
			int orderInProj = positionOf[itemIx];
			final double objValue = projection[orderInProj].getSecond();

			ArrayList<Pair> cart = new ArrayList<Pair>(numOfItems);

			cart.add(new Pair(itemIx, 0.0));

			int leftIx = orderInProj - 1;
			int rightIx = orderInProj + 1;
			for (int j = 1; j < numOfItems - 1; j++)
			{
				double leftDist = MAX_VALUE;
				double rightDist = MAX_VALUE;
				if (leftIx >= 0)
				{
					leftDist = abs(objValue - projection[leftIx].getSecond());
				}

				if (rightIx < numOfItems)
				{
					rightDist = abs(objValue - projection[rightIx].getSecond());
				}

				if (leftDist < rightDist)
				{
					cart.add(new Pair(projection[leftIx].getFirst(), leftDist));
					leftIx--;
				} else
				{
					cart.add(new Pair(projection[rightIx].getFirst(), rightDist));
					rightIx++;
				}
			}
			if (p != null)
			{
				writeCartToFile(cart, p, false);
			} else
			{
				carts.add(cart);
			}
		}
	}

	public List<ArrayList<Pair>> getCarts()
	{
		return carts;
	}

	public void writeCartsToFile(String filename, boolean includeDistance)
	{
		try
		{
			PrintStream p = new PrintStream(new FileOutputStream(filename));
			for (ArrayList<Pair> cart : this.carts)
			{
				writeCartToFile(cart, p, includeDistance);
			}
			new FileOutputStream(filename).close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static void writeCartToFile(ArrayList<Pair> cart, PrintStream p,
			boolean includeDistance)
	{
		String str = "";
		for (Pair value : cart)
		{
			str += value.getFirst() + " ";
			if (includeDistance)
			{
				str += value.getSecond() + " ";
			}
		}
		p.println(str.trim());
	}

	private static class Pair extends mime.tool.Pair<Integer, Double> implements
			Comparable<Pair>
	{
		public Pair(Integer first, Double second)
		{
			super(first, second);
		}

		@Override
		public int compareTo(Pair o)
		{
			return this.getSecond().compareTo(o.getSecond());
		}
	}
}
