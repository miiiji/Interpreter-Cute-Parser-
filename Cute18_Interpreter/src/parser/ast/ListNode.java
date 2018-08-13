package parser.ast;

public interface ListNode extends Node { // 새로 수정된 ListNode
	Node car();

	ListNode cdr();
	
	

	static boolean equal_list(ListNode temp1, ListNode temp2) {
		if ((temp1.car() == null) && (temp2.car() == null)) {
			return true;
		} else if ((temp1.car() == null) && (temp2.car() != null) || (temp1.car() != null) && (temp2.car() == null)) {
			return false;
		} else if ((temp1.car() != null) && (temp2.car() != null)) {
			if (temp1.car().toString().equals(temp2.car().toString())) {
				return equal_list(temp1.cdr(), temp2.cdr());
			} else {
				return false;
			}
		}
		return false;
	}

	static ListNode cons(Node head, ListNode tail) {
		return new ListNode() {
			@Override
			public Node car() {
				return head;
			}

			@Override
			public ListNode cdr() {
				return tail;
			}

			public String toString() {
				if (head != null && tail != null) {
					return head.toString() + tail.toString();
				}
				return null;
			}// 값들을 string으로 바꾸어 비교하기 위해 head값과 tail값을 각각 toString화 해서 합쳐준다

		};
	}

	static ListNode EMPTYLIST = new ListNode() {

		@Override
		public Node car() {
			return null;
		}

		@Override
		public ListNode cdr() {
			return null;
		}
	};
	static ListNode ENDLIST = new ListNode() {
		@Override
		public Node car() {
			return null;
		}

		@Override
		public ListNode cdr() {
			return null;
		}
	};

}