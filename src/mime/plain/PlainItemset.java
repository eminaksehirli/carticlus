/* A class that represents itemsets.
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

import static java.util.Collections.unmodifiableSet;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class PlainItemset implements Collection<PlainItem>
{
	private final SortedSet<PlainItem> items;

	public PlainItemset()
	{
		this.items = new TreeSet<PlainItem>();
	}

	public PlainItemset(PlainItem... newItems)
	{
		this();
		for (PlainItem newItem : newItems)
		{
			this.items.add(newItem);
		}
	}

	public PlainItemset(PlainItemset aPlainItemset)
	{
		this.items = new TreeSet<PlainItem>(aPlainItemset.items);
	}

	public PlainItemset(Iterable<PlainItem> items)
	{
		this();
		for (PlainItem item : items)
		{
			this.items.add(item);
		}
	}

	public TidList getTIDs()
	{
		// TODO check if BitSet can stay
		TidList tids = new TidList();
		Iterator<PlainItem> it = this.items.iterator();
		if (it.hasNext())
		{
			tids.or(it.next().getTIDs()); // setup

			for (; it.hasNext();)
			{
				tids.and(it.next().getTIDs());
			}
		}
		return tids;
	}

	@Override
	public boolean add(PlainItem newItem)
	{
		return this.items.add(newItem);
	}

	@Override
	public boolean addAll(Collection<? extends PlainItem> plainItemSet)
	{
		return this.items.addAll(plainItemSet);
	}

	@Override
	public boolean remove(Object itemToRemove)
	{
		return this.items.remove(itemToRemove);
	}

	@Override
	public int size()
	{
		return this.items.size();
	}

	public boolean contains(PlainItem item)
	{
		return this.items.contains(item);
	}

	public Set<PlainItem> asCollection()
	{
		return unmodifiableSet(this.items);
	}

	public boolean containsAll(PlainItemset head)
	{
		return this.items.containsAll(head.items);
	}

	@Override
	public String toString()
	{
		return this.items.toString();
	}

	@Override
	public Iterator<PlainItem> iterator()
	{
		return asCollection().iterator();
	}

	@Override
	public int hashCode()
	{
		return this.items.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlainItemset other = (PlainItemset) obj;
		if (this.items == null)
		{
			if (other.items != null)
				return false;
		} else if (other.items.size() != this.items.size())
		{
			return false;
		} else
		{
			for (PlainItem plainItem : other.items)
			{
				if (!this.items.contains(plainItem))
				{
					return false;
				}
			}
		}
		return true;
	}

	public final static class ItemSetSizeComparator implements
			Comparator<PlainItemset>
	{
		@Override
		public int compare(PlainItemset o1, PlainItemset o2)
		{
			int overSize = o2.size() - o1.size();
			if (overSize == 0)
			{
				int overCardinality = o2.getTIDs().cardinality()
						- o1.getTIDs().cardinality();
				if (overCardinality == 0)
				{
					if (o1.equals(o2))
					{
						return 0;
					}

					Iterator<PlainItem> firstIterator = o1.iterator();
					Iterator<PlainItem> secondIterator = o2.iterator();

					for (; firstIterator.hasNext();)
					{
						PlainItem item2 = secondIterator.next();
						PlainItem item1 = firstIterator.next();
						int overItems = item1.compareTo(item2);
						if (overItems != 0)
						{
							return overItems;
						}
					}
					return 0;
				}
				return overCardinality;
			}
			return overSize;
		}
	}

	public final static class SupportComparator implements
			Comparator<PlainItemset>
	{

		@Override
		public int compare(PlainItemset o1, PlainItemset o2)
		{
			return o2.getTIDs().cardinality() - o1.getTIDs().cardinality();
		}
	}

	@Override
	public boolean isEmpty()
	{
		return items.isEmpty();
	}

	@Override
	public boolean contains(Object o)
	{
		return items.contains(o);
	}

	@Override
	public Object[] toArray()
	{
		return items.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		return items.toArray(a);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return items.containsAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		return items.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		return items.retainAll(c);
	}

	@Override
	public void clear()
	{
		items.clear();
	}

}
