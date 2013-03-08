/* A class that represents TidLists as Bitsets.
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
package mime.plain;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

public class TidList extends BitSet
{
	private static final long serialVersionUID = 6037248886409149654L;

	public TidList()
	{
		super();
	}

	public TidList(BitSet t)
	{
		super();
		or(t);
	}

	/**
	 * Adds the first numOfTransactions transactions to this item
	 * 
	 * @param numOfTransactions
	 */
	public TidList(int numOfTransactions)
	{
		this.set(0, numOfTransactions);
	}

	private boolean containsAll(BitSet s1, BitSet s2)
	{
		int index = -1;
		while ((index = s2.nextSetBit(index + 1)) != -1)
		{
			if (!s1.get(index))
			{
				return false;
			}
		}
		return true;
	}

	public static int containsNumber(BitSet s1, BitSet s2)
	{
		int total = 0;
		int index = -1;
		while ((index = s2.nextSetBit(index + 1)) != -1)
		{
			if (s1.get(index))
			{
				total++;
			}
		}
		return total;
	}

	public boolean isSubsetOf(BitSet c)
	{
		return containsAll(c, this);
	}

	public void addAll(TidList t)
	{
		this.or(t);
	}

	public void retainAll(TidList t)
	{
		this.and(t);
	}

	public void removeAll(TidList t)
	{
		this.andNot(t);
	}

	public void add(int i)
	{
		this.set(i, true);
	}

	@Override
	public int size()
	{
		return cardinality();
	}

	public static TidList intersect(TidList t1, TidList t2)
	{
		TidList i;

		if (t1.cardinality() < t2.cardinality())
		{
			i = new TidList(t1);
			i.and(t2);
		} else
		{
			i = new TidList(t2);
			i.and(t1);
		}
		return i;
	}

	public static TidList difference(TidList t1, TidList t2)
	{
		TidList i = new TidList(t1);
		i.removeAll(t2);
		return i;
	}

	public List<Integer> toList()
	{
		List<Integer> l = new LinkedList<Integer>();
		int index = -1;
		while ((index = nextSetBit(index + 1)) != -1)
		{
			l.add(index);
		}
		return l;
	}
}
