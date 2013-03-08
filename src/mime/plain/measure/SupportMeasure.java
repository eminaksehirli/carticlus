/* Support measure that uses TID lists.
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

package mime.plain.measure;

import mime.plain.PlainItem;
import mime.plain.PlainItemset;
import mime.plain.TidList;

public class SupportMeasure implements ItemsetMeasure
{
	@Override
	public String getName()
	{
		return "Support";
	}

	@Override
	public double evaluate(PlainItemset itemSet)
	{
		return itemSet.getTIDs().cardinality();
	}

	@Override
	public double evaluate(PlainItemset itemSet, PlainItem item)
	{
		if (itemSet.size() == 0)
		{
			return item.getTIDs().cardinality();
		}
		return TidList.intersect(itemSet.getTIDs(), new TidList(item.getTIDs()))
				.cardinality();
	}
}
