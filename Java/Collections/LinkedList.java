/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;

/**
 * LinkedList是基于双向循环链表（从源码中可以很容易看出）实现的，除了可以当作链表来操作外，它还可以当作栈，队列和双端队列来使用。
 * 
 * LinkedList同样是非线程安全的，只在单线程下适合使用。
 * 
 * LinkedList实现了Serializable接口，因此它支持序列化，能够通过序列化传输，实现了Cloneable接口，能被克隆。
 * 
 */
public class LinkedList<E> extends AbstractSequentialList<E>
		implements List<E>, Deque<E>, Queue<E>, Cloneable, Serializable {

	private static final long serialVersionUID = 876323262645176354L;

	transient int size = 0;

	/**
	 * 链表的头结点
	 */
	transient Link<E> voidLink;

	/**
	 * Link表示链表的一个结点，其中ET表示其中数据的类型
	 * 
	 * 每个Link元素中有三个属性
	 * 
	 * ET类型的数据，Link类型的前驱和后继
	 */
	private static final class Link<ET> {

		ET data;

		Link<ET> previous, next;

		Link(ET o, Link<ET> p, Link<ET> n) {
			data = o;
			previous = p;
			next = n;
		}
	}

	
	/**
	 * 在默认构造方法中只有一个头结点，这个头结点前驱和后继都指向自己
	 */
	public LinkedList() {
		voidLink = new Link<E>(null, null, null);
		voidLink.previous = voidLink;
		voidLink.next = voidLink;
	}


	public LinkedList(Collection<? extends E> collection) {
		this();
		addAll(collection);
	}

	@Override
	public void add(int location, E object) {
		if (location >= 0 && location <= size) {
			
			// 首先获得第一个头结点,根据这个结点寻址
			Link<E> link = voidLink;
			
			/*
			 * 根据二分法的思想，如果插入位置小于(size / 2)，从前向后寻址，否则从后向前寻址
			 * 获取到第location位置的结点
			 */
			if (location < (size / 2)) {
				for (int i = 0; i <= location; i++) {
					link = link.next;
				}
			} else {
				for (int i = size; i > location; i--) {
					link = link.previous;
				}
			}
			
			
			//previous则是第location-1个结点
			Link<E> = link.previous;
			// newLink是新的将要插入的结点，这是指定前驱和后继
			Link<E> newLink = new Link<E>(object, previous, link);
			//第四个的后继，指向新的要插入的结点newLink
			previous.next = newLink;
			// link是原本第五个位置的结点，现在把link的前驱指向newLink，那么link则变成了第六个位置的结点
			link.previous = newLink;
			
			size++;
			modCount++;
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	/**
	 * 默认插入到最后
	 */
	@Override
	public boolean add(E object) {
		return addLastImpl(object);
	}

	private boolean addLastImpl(E object) {
		// 头结点的前驱就是原本最后一个结点
		Link<E> oldLast = voidLink.previous;
		// 创建新结点，前驱和后继分别指向oldLast和voidLink
		Link<E> newLink = new Link<E>(object, oldLast, voidLink);
		// 将头结点的前驱修改成新结点
		voidLink.previous = newLink;
		// 把原最后位置的结点的后继该为新结点
		oldLast.next = newLink;
		
		size++;
		modCount++;
		return true;
	}

	@Override
	public boolean addAll(int location, Collection<? extends E> collection) {
		if (location < 0 || location > size) {
			throw new IndexOutOfBoundsException();
		}
		int adding = collection.size();
		if (adding == 0) {
			return false;
		}
		Collection<? extends E> elements = (collection == this) ? new ArrayList<E>(collection) : collection;

		// 寻址，找到第（location-1)）位置的结点
		Link<E> previous = voidLink;
		if (location < (size / 2)) {
			for (int i = 0; i < location; i++) {
				previous = previous.next;
			}
		} else {
			for (int i = size; i >= location; i--) {
				previous = previous.previous;
			}
		}
		// 将整个结合插入到previous和next之间，这个next结点是要插入集合的最后一个元素的后继
		Link<E> next = previous.next;
		// 按照顺序将要插入的结点依次插入
		for (E e : elements) {
			// 创建一个新结点，插入到集合中，首先指定前驱为previous
			Link<E> newLink = new Link<E>(e, previous, null);
			// 指定previous的后继为newLink
			previous.next = newLink;
			// 每次插入后新的previous为新插入的结点，便于下一个结点的插入
			previous = newLink;
		}
		
		// 所有元素插入后，previous标识插入集合的最后一个元素，将previous后继指向next，next的前驱指向previous
		previous.next = next;
		next.previous = previous;
		
		size += adding;
		modCount++;
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> collection) {
		int adding = collection.size();
		if (adding == 0) {
			return false;
		}
		Collection<? extends E> elements = (collection == this) ? new ArrayList<E>(collection) : collection;

		Link<E> previous = voidLink.previous;
		for (E e : elements) {
			Link<E> newLink = new Link<E>(e, previous, null);
			previous.next = newLink;
			previous = newLink;
		}
		previous.next = voidLink;
		voidLink.previous = previous;
		size += adding;
		modCount++;
		return true;
	}

	public void addFirst(E object) {
		addFirstImpl(object);
	}

	/**
	 * 首位插入
	 */
	private boolean addFirstImpl(E object) {
		/*
		 * 获取oldFirst，
		 * 创建一个新结点，前驱为头结点，后继为oldFirst
		 * 指定头结点的后继为新结点，
		 * 指定oldFirst的前驱为新结点
		 */
		Link<E> oldFirst = voidLink.next;
		Link<E> newLink = new Link<E>(object, voidLink, oldFirst);
		voidLink.next = newLink;
		oldFirst.previous = newLink;
		
		
		size++;
		modCount++;
		return true;
	}

	public void addLast(E object) {
		addLastImpl(object);
	}

	@Override
	public void clear() {
		if (size > 0) {
			size = 0;
			
			/**
			 * 集合清除，只要将头结点voidLink的前驱后继都指向自身即可
			 */
			voidLink.next = voidLink;
			voidLink.previous = voidLink;
			modCount++;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object clone() {
		try {
			LinkedList<E> l = (LinkedList<E>) super.clone();
			l.size = 0;
			l.voidLink = new Link<E>(null, null, null);
			l.voidLink.previous = l.voidLink;
			l.voidLink.next = l.voidLink;
			l.addAll(this);
			return l;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}

	@Override
	public boolean contains(Object object) {
		// 首先判断不是空结点
		Link<E> link = voidLink.next;
		if (object != null) {
			while (link != voidLink) {
				if (object.equals(link.data)) {
					return true;
				}
				link = link.next;
			}
		} else {
			while (link != voidLink) {
				if (link.data == null) {
					return true;
				}
				link = link.next;
			}
		}
		return false;
	}

	@Override
	public E get(int location) {
		if (location >= 0 && location < size) {
			Link<E> link = voidLink;
			// 通过二分法的思想，寻址
			if (location < (size / 2)) {
				for (int i = 0; i <= location; i++) {
					link = link.next;
				}
			} else {
				for (int i = size; i > location; i--) {
					link = link.previous;
				}
			}
			return link.data;
		}
		throw new IndexOutOfBoundsException();
	}

	public E getFirst() {
		return getFirstImpl();
	}

	/**
	 * 获取第一个，就是头结点的后继
	 */
	private E getFirstImpl() {
		Link<E> first = voidLink.next;
		if (first != voidLink) {
			return first.data;
		}
		throw new NoSuchElementException();
	}

	/**
	 * 获取最后一个，就是头结点的前驱
	 */
	public E getLast() {
		Link<E> last = voidLink.previous;
		if (last != voidLink) {
			return last.data;
		}
		throw new NoSuchElementException();
	}

	@Override
	public int indexOf(Object object) {
		int pos = 0;
		Link<E> link = voidLink.next;
		if (object != null) {
			// 寻址，每比较一个元素pos加1
			while (link != voidLink) {
				if (object.equals(link.data)) {
					return pos;
				}
				link = link.next;
				pos++;
			}
		} else {
			while (link != voidLink) {
				if (link.data == null) {
					return pos;
				}
				link = link.next;
				pos++;
			}
		}
		return -1;
	}

	
	/**
	 * 要想获取最后一个的位置，从后向前寻址
	 */
	@Override
	public int lastIndexOf(Object object) {
		int pos = size;
		Link<E> link = voidLink.previous;
		if (object != null) {
			while (link != voidLink) {
				pos--;
				if (object.equals(link.data)) {
					return pos;
				}
				link = link.previous;
			}
		} else {
			while (link != voidLink) {
				pos--;
				if (link.data == null) {
					return pos;
				}
				link = link.previous;
			}
		}
		return -1;
	}
	
	
	@Override
	public ListIterator<E> listIterator(int location) {
		return new LinkIterator<E>(this, location);
	}

	
	/**
	 * 删除指定位置的元素
	 */
	@Override
	public E remove(int location) {
		if (location >= 0 && location < size) {
			Link<E> link = voidLink;
			
			// 找到第location位置的结点
			if (location < (size / 2)) {
				for (int i = 0; i <= location; i++) {
					link = link.next;
				}
			} else {
				for (int i = size; i > location; i--) {
					link = link.previous;
				}
			}
			
			// 获取link结点的前驱和后继
			Link<E> previous = link.previous;
			Link<E> next = link.next;
			
			previous.next = next;
			next.previous = previous;
			size--;
			
			modCount++;
			return link.data;
		}
		throw new IndexOutOfBoundsException();
	}

	@Override
	public boolean remove(Object object) {
		return removeFirstOccurrenceImpl(object);
	}

	public E removeFirst() {
		return removeFirstImpl();
	}

	
	private E removeFirstImpl() {
		Link<E> first = voidLink.next;
		if (first != voidLink) {
			// 首先获取first的后继，并将后继的前驱指向头结点，将头结点的后继指向next
			Link<E> next = first.next;
			voidLink.next = next;
			next.previous = voidLink;
			size--;
			modCount++;
			return first.data;
		}
		throw new NoSuchElementException();
	}

	public E removeLast() {
		return removeLastImpl();
	}

	private E removeLastImpl() {
		// 获取最后一个位置的结点
		Link<E> last = voidLink.previous;
		if (last != voidLink) {
			// 获取last的前驱previous，将previous的后继指向头结点，将头结点的前驱指向previous
			Link<E> previous = last.previous;
			voidLink.previous = previous;
			previous.next = voidLink;
			size--;
			
			modCount++;
			return last.data;
		}
		throw new NoSuchElementException();
	}

	public Iterator<E> descendingIterator() {
		return new ReverseLinkIterator<E>(this);
	}

	public boolean offerFirst(E e) {
		return addFirstImpl(e);
	}

	public boolean offerLast(E e) {
		return addLastImpl(e);
	}

	public E peekFirst() {
		return peekFirstImpl();
	}

	public E peekLast() {
		Link<E> last = voidLink.previous;
		return (last == voidLink) ? null : last.data;
	}

	public E pollFirst() {
		return (size == 0) ? null : removeFirstImpl();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Deque#pollLast()
	 * @since 1.6
	 */
	public E pollLast() {
		return (size == 0) ? null : removeLastImpl();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Deque#pop()
	 * @since 1.6
	 */
	public E pop() {
		return removeFirstImpl();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Deque#push(java.lang.Object)
	 * @since 1.6
	 */
	public void push(E e) {
		addFirstImpl(e);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Deque#removeFirstOccurrence(java.lang.Object)
	 * @since 1.6
	 */
	public boolean removeFirstOccurrence(Object o) {
		return removeFirstOccurrenceImpl(o);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Deque#removeLastOccurrence(java.lang.Object)
	 * @since 1.6
	 */
	public boolean removeLastOccurrence(Object o) {
		Iterator<E> iter = new ReverseLinkIterator<E>(this);
		return removeOneOccurrence(o, iter);
	}

	private boolean removeFirstOccurrenceImpl(Object o) {
		Iterator<E> iter = new LinkIterator<E>(this, 0);
		return removeOneOccurrence(o, iter);
	}

	private boolean removeOneOccurrence(Object o, Iterator<E> iter) {
		while (iter.hasNext()) {
			E element = iter.next();
			if (o == null ? element == null : o.equals(element)) {
				iter.remove();
				return true;
			}
		}
		return false;
	}

	@Override
	public E set(int location, E object) {
		if (location >= 0 && location < size) {
			Link<E> link = voidLink;
			if (location < (size / 2)) {
				for (int i = 0; i <= location; i++) {
					link = link.next;
				}
			} else {
				for (int i = size; i > location; i--) {
					link = link.previous;
				}
			}
			E result = link.data;
			link.data = object;
			return result;
		}
		throw new IndexOutOfBoundsException();
	}

	/**
	 * Returns the number of elements in this {@code LinkedList}.
	 *
	 * @return the number of elements in this {@code LinkedList}.
	 */
	@Override
	public int size() {
		return size;
	}

	public boolean offer(E o) {
		return addLastImpl(o);
	}

	public E poll() {
		return size == 0 ? null : removeFirst();
	}

	public E remove() {
		return removeFirstImpl();
	}

	public E peek() {
		return peekFirstImpl();
	}

	private E peekFirstImpl() {
		Link<E> first = voidLink.next;
		return first == voidLink ? null : first.data;
	}

	public E element() {
		return getFirstImpl();
	}

	/**
	 * Returns a new array containing all elements contained in this
	 * {@code LinkedList}.
	 *
	 * @return an array of the elements from this {@code LinkedList}.
	 */
	@Override
	public Object[] toArray() {
		int index = 0;
		Object[] contents = new Object[size];
		Link<E> link = voidLink.next;
		while (link != voidLink) {
			contents[index++] = link.data;
			link = link.next;
		}
		return contents;
	}

	/**
	 * Returns an array containing all elements contained in this
	 * {@code LinkedList}. If the specified array is large enough to hold the
	 * elements, the specified array is used, otherwise an array of the same
	 * type is created. If the specified array is used and is larger than this
	 * {@code LinkedList}, the array element following the collection elements
	 * is set to null.
	 *
	 * @param contents
	 *            the array.
	 * @return an array of the elements from this {@code LinkedList}.
	 * @throws ArrayStoreException
	 *             if the type of an element in this {@code LinkedList} cannot
	 *             be stored in the type of the specified array.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] contents) {
		int index = 0;
		if (size > contents.length) {
			Class<?> ct = contents.getClass().getComponentType();
			contents = (T[]) Array.newInstance(ct, size);
		}
		Link<E> link = voidLink.next;
		while (link != voidLink) {
			contents[index++] = (T) link.data;
			link = link.next;
		}
		if (index < contents.length) {
			contents[index] = null;
		}
		return contents;
	}

	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		stream.writeInt(size);
		Iterator<E> it = iterator();
		while (it.hasNext()) {
			stream.writeObject(it.next());
		}
	}

	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		size = stream.readInt();
		voidLink = new Link<E>(null, null, null);
		Link<E> link = voidLink;
		for (int i = size; --i >= 0;) {
			Link<E> nextLink = new Link<E>((E) stream.readObject(), link, null);
			link.next = nextLink;
			link = nextLink;
		}
		link.next = voidLink;
		voidLink.previous = link;
	}
	
	private static final class LinkIterator<ET> implements ListIterator<ET> {
		int pos, expectedModCount;

		final LinkedList<ET> list;

		Link<ET> link, lastLink;

		LinkIterator(LinkedList<ET> object, int location) {
			list = object;
			expectedModCount = list.modCount;
			if (location >= 0 && location <= list.size) {
				// pos ends up as -1 if list is empty, it ranges from -1 to
				// list.size - 1
				// if link == voidLink then pos must == -1
				link = list.voidLink;
				if (location < list.size / 2) {
					for (pos = -1; pos + 1 < location; pos++) {
						link = link.next;
					}
				} else {
					for (pos = list.size; pos >= location; pos--) {
						link = link.previous;
					}
				}
			} else {
				throw new IndexOutOfBoundsException();
			}
		}

		public void add(ET object) {
			if (expectedModCount == list.modCount) {
				Link<ET> next = link.next;
				Link<ET> newLink = new Link<ET>(object, link, next);
				link.next = newLink;
				next.previous = newLink;
				link = newLink;
				lastLink = null;
				pos++;
				expectedModCount++;
				list.size++;
				list.modCount++;
			} else {
				throw new ConcurrentModificationException();
			}
		}

		public boolean hasNext() {
			return link.next != list.voidLink;
		}

		public boolean hasPrevious() {
			return link != list.voidLink;
		}

		public ET next() {
			if (expectedModCount == list.modCount) {
				LinkedList.Link<ET> next = link.next;
				if (next != list.voidLink) {
					lastLink = link = next;
					pos++;
					return link.data;
				}
				throw new NoSuchElementException();
			}
			throw new ConcurrentModificationException();
		}

		public int nextIndex() {
			return pos + 1;
		}

		public ET previous() {
			if (expectedModCount == list.modCount) {
				if (link != list.voidLink) {
					lastLink = link;
					link = link.previous;
					pos--;
					return lastLink.data;
				}
				throw new NoSuchElementException();
			}
			throw new ConcurrentModificationException();
		}

		public int previousIndex() {
			return pos;
		}

		public void remove() {
			if (expectedModCount == list.modCount) {
				if (lastLink != null) {
					Link<ET> next = lastLink.next;
					Link<ET> previous = lastLink.previous;
					next.previous = previous;
					previous.next = next;
					if (lastLink == link) {
						pos--;
					}
					link = previous;
					lastLink = null;
					expectedModCount++;
					list.size--;
					list.modCount++;
				} else {
					throw new IllegalStateException();
				}
			} else {
				throw new ConcurrentModificationException();
			}
		}

		public void set(ET object) {
			if (expectedModCount == list.modCount) {
				if (lastLink != null) {
					lastLink.data = object;
				} else {
					throw new IllegalStateException();
				}
			} else {
				throw new ConcurrentModificationException();
			}
		}
	}

	/*
	 * NOTES:descendingIterator is not fail-fast, according to the documentation
	 * and test case.
	 */
	private class ReverseLinkIterator<ET> implements Iterator<ET> {
		private int expectedModCount;

		private final LinkedList<ET> list;

		private Link<ET> link;

		private boolean canRemove;

		ReverseLinkIterator(LinkedList<ET> linkedList) {
			list = linkedList;
			expectedModCount = list.modCount;
			link = list.voidLink;
			canRemove = false;
		}

		public boolean hasNext() {
			return link.previous != list.voidLink;
		}

		public ET next() {
			if (expectedModCount == list.modCount) {
				if (hasNext()) {
					link = link.previous;
					canRemove = true;
					return link.data;
				}
				throw new NoSuchElementException();
			}
			throw new ConcurrentModificationException();

		}

		public void remove() {
			if (expectedModCount == list.modCount) {
				if (canRemove) {
					Link<ET> next = link.previous;
					Link<ET> previous = link.next;
					next.next = previous;
					previous.previous = next;
					link = previous;
					list.size--;
					list.modCount++;
					expectedModCount++;
					canRemove = false;
					return;
				}
				throw new IllegalStateException();
			}
			throw new ConcurrentModificationException();
		}
	}

}
