/* A simple database of items.
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

import static java.lang.Integer.parseInt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PlainItemDB implements Iterable<PlainItem>
{
	private final Map<Integer, PlainItem> items;

	public PlainItemDB()
	{
		this.items = new HashMap<Integer, PlainItem>();
	}

	public PlainItem get(int itemId)
	{
		PlainItem item = this.items.get(itemId);
		if (item == null)
		{
			item = new PlainItem(itemId);
			this.items.put(itemId, item);
		}

		return item;
	}

	public PlainItem get(String itemId)
	{
		return get(parseInt(itemId.trim()));
	}

	public int size()
	{
		return this.items.size();
	}

	@Override
	public Iterator<PlainItem> iterator()
	{
		return this.items.values().iterator();
	}
}
