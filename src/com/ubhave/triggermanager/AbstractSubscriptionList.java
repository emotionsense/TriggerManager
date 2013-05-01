/* **************************************************
 Copyright (c) 2012, University of Cambridge
 Neal Lathia, neal.lathia@cl.cam.ac.uk
 Kiran Rachuri, kiran.rachuri@cl.cam.ac.uk

This library was developed as part of the EPSRC Ubhave (Ubiquitous and
Social Computing for Positive Behaviour Change) Project. For more
information, please visit http://www.emotionsense.org

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ************************************************** */

package com.ubhave.triggermanager;

import java.util.Random;

import android.util.SparseArray;

public abstract class AbstractSubscriptionList<H>
{
	private final Random keyGenerator;
	protected final SparseArray<H> map;

	public AbstractSubscriptionList()
	{
		keyGenerator = new Random();
		map = new SparseArray<H>();
	}
	
	public void add(int key, H s) throws TriggerException
	{
		map.append(key, s);
	}

	public void remove(int id) throws TriggerException
	{
		H s = map.get(id);
		if (s != null)
		{
			map.delete(id);
		}
	}

	public H get(int id)
	{
		return map.get(id);
	}

	public int randomKey() throws TriggerException
	{
		int id = keyGenerator.nextInt();
		int loopCount = 0;
		while (get(id) != null)
		{
			if (loopCount > 1000)
			{
				throw new TriggerException(TriggerException.INVALID_STATE, "Listener map >1000 key conflicts.");
			}
			id = keyGenerator.nextInt();
			loopCount++;
		}
		return id;
	}
}
