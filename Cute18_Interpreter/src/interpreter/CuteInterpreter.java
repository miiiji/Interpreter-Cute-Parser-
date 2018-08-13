package interpreter;

import parser.ast.*;
import java.util.HashMap;

//201402420전형진, 201601974김미지 ITEM3
public class CuteInterpreter {
	ListNode temp;

	/* Define function을 이용해서 Node를 바인딩할때 저장하기 위한 HashMap */
	public static HashMap<String, Node> defined_map = new HashMap<String, Node>();

	static void insertTable(String id, Node value) { // table에 바인딩 하는 함수
		defined_map.put(id, value);
	}

	static Node lookupTable(Node rootnode) { // 테이블에 원하는 것이 존재 하는 경우 반환해주는 함수
		if (defined_map.containsKey(((IdNode) rootnode).getidStr())) {
			return defined_map.get(((IdNode) rootnode).getidStr());
		} else {
			return rootnode;
		}
	}

	private void errorLog(String err) { // 에러 로그
		System.out.print(err);
	}

	public Node runExpr(Node rootExpr) {
		// TODO Auto-generated method stub
		if (rootExpr == null)
			return null;
		if (rootExpr instanceof IdNode) {
			rootExpr = lookupTable(((IdNode) rootExpr));
			return rootExpr;
		} else if (rootExpr instanceof IntNode)
			return rootExpr;
		else if (rootExpr instanceof BooleanNode)
			return rootExpr;
		else if (rootExpr instanceof ListNode) {
			return runList((ListNode) rootExpr);
		} else if (rootExpr instanceof QuoteNode) {
			return rootExpr;
		} else
			errorLog("run Expr error");
		return null;
	}

	private Node runList(ListNode list) {
		if (list.equals(ListNode.EMPTYLIST))
			return list;
		if (list.car() instanceof FunctionNode) {
			return runFunction((FunctionNode) list.car(), list.cdr());
		}
		if (list.car() instanceof BinaryOpNode) {
			return runBinary(list);
		}
		if (list.car() instanceof ListNode) { // list.car()가 ListNode인 경우
			int lambda_operator_counter = 0; // 카운터를 이용
			int lambda_bind_counter = 0; // 카운터를 이용
			temp = list.cdr(); // 전역 변수 temp에 list.cdr을 저장
			ListNode list_for_while = temp; // while문에서 사용될 변수
			if ((ListNode) ((ListNode) list.car()).cdr().car() instanceof ListNode) { // list.car.cdr.car이
																						// ListNode인
																						// 경우
																						// 처리
				ListNode insert_temp = (ListNode) ((ListNode) list.car()).cdr().car(); // 걸어나가는
																				// 변수
																				// p이용
				while (insert_temp != null && list_for_while != null) { // while loop의 조건식
					if (insert_temp.car() instanceof IdNode) { // p.car가 IdNode인 경우
						lambda_operator_counter++; // 카운터 증가
					}
					if (list_for_while.car() instanceof IntNode) { // 선언한 변수가
																	// IntNode인
																	// 경우
						lambda_bind_counter++; // 카운터 증가
					} else if (list_for_while.car() instanceof IdNode) {
						lambda_bind_counter++; // 카운터 증가
					}
					insert_temp = insert_temp.cdr(); // 걸어나가는 변수 p
					list_for_while = list_for_while.cdr(); // 뒤의 부분을 확인 하기 위한
															// 변수도 걸어나가는 변수 p처럼
															// 뒤로 보냄 즉, next와 같은
															// 기능
				}
				if (lambda_operator_counter != lambda_bind_counter) { // 앞에서 비교된
																		// 두
																		// 카운터를
																		// 비교 다른
																		// 경우
					return runExpr(list.car()); // list를 반환
				} else { // 같은 경우
					return runExpr(list.car()); // list.car를 runexpr한것을 반환
				}
			}
		}
		if (list.car() instanceof IdNode) { // list.car가 IdNode인 경우
			IdNode binded_id = ((IdNode) list.car()); //
			Node temp_run_node = ListNode.cons(lookupTable(binded_id), list.cdr());
			return runExpr(temp_run_node);
		}
		return list;
	}

	private Node runFunction(FunctionNode operator, ListNode operand) {
		 Node temp1 = null;// 임시로 저장할 temp1
	      Node temp2 = null;// 임시로 저장할 temp2
	      if (operand.cdr() != null) {// null 포인트 예외처리를 해 주기위해 조건문검 .operand의 cdr이
	                           // null이 아니고
	         if (operand.cdr().car() instanceof ListNode) {// operand.cdr의 car가
	                                             // ListNode이고
	            if ((ListNode) (operand.cdr().car()) != null) {//operand의 cdr의 car가 null이 아니고 
	               if (((ListNode) (operand.cdr().car())).cdr() != null) {//operande cdr의 car의 cdr이 null이 아니고 
	                  if (((ListNode) (operand.cdr().car())).car() instanceof FunctionNode) {//operand의 cdr의 car의 car가 FunctionNode일 때 (LAMBDA)
	                     if (((FunctionNode) (((ListNode) (operand.cdr().car())).car())).value
	                           .equals(FunctionNode.FunctionType.LAMBDA)) {//또한 operand의 cdr의 car의 car이 LAMBDA이고  
	                        if (((ListNode) (operand.cdr().car())).cdr().cdr() != null) {//그것의 cdr이 null이아니고 
	                           if (((ListNode) (operand.cdr().car())).cdr().cdr().car() != null) {//그것의 car가 null이 아닐 때 
	                              Node jcm = ((ListNode) (operand.cdr().car())).cdr().cdr().car();//jcm에 그것의 car를 저장 해 놓고 
	                              if ((((ListNode) jcm).car()) instanceof FunctionNode) {//만약 그게 FunctionNode의 타입이라면 
	                                 if (((FunctionNode) (((ListNode) jcm).car())).value
	                                       .equals(FunctionNode.FunctionType.DEFINE)) {//또한 그때 그 value 값이 Define이 라면 
	                                    String nesting_bind_str = ((IdNode) ((ListNode) jcm).cdr().car())
	                                          .getidStr();//jcm의 cdr의 car 부분을 nesting_bind_str에 저장하고 
	                                    insertTable(nesting_bind_str, ((ListNode) jcm).cdr().cdr().car());//insertTable을 이용해서 string 값과 jcm의 cdr의 cdr의 car을 넣는다 
	                                    temp1 = ((ListNode) (operand.cdr().car())).car();//temp1에 operand의 연산 제외한 다음 값과 
	                                    temp2 = ((ListNode) ((ListNode) (operand.cdr().car())).cdr()).car();// temp2 에는 그 이후의 값을 저장하고 
	                                    QuoteNode a = new QuoteNode(temp2);//temp2를 QuoteNode로 새로 생성을 한다 
	                                    Node first_temp = ListNode.cons(a.quoted, (((ListNode) temp2).cdr()));//cons를 이용해 a.quoted 값과 temp2의 cdr을 합치고 
	                                    temp2 = ListNode.cons(temp2, (ListNode) first_temp);//temp2를 이용해 그것 과 first_temp값을 합친다 
	                                    Node final_temp = ListNode.cons(temp1, ((ListNode) temp2).cdr()); //final _temp는 temp1과 temp2의 cdr값을 cons로 합친다 

	                                    Node one = ((ListNode) final_temp).car();//one에는 final_temp의 car값과 
	                                    Node two = ((ListNode) final_temp).cdr();//two 에는 fianl_temp의 cdr값을 저장한다 
	                                    Node real_final = ListNode.cons(((ListNode) two).car(),
	                                          (ListNode) ((ListNode) (operand.cdr().car())).cdr().cdr()
	                                                .cdr());//맨 뒤에서 부터 합치기위해 two의 car부분과 뒤쪽에 저장 해 둔 operand의 cdr.car.cdr.cdr를 cons로 합치고 
	                                    real_final = ListNode.cons(one, (ListNode) real_final);//마지막으로 맨 앞의 값 까지 합치기위해 cons로 one과 real_final을 합친다 
	                                    ListNode nesting = (ListNode) real_final;//nesting이라는 변수에 real_final을 ListNode로 형 변환해서 저장하고 

	                                    insertTable(((IdNode) (operand.car())).getidStr(), nesting);//insertTable을 이용해서 operand의 car의 string 값을 넣고, nesting 변수에 저장한다 
	                                    return BooleanNode.TRUE_NODE;//True 값을 return 한다.
	                                 }
	                              }
	                           }
	                        }
	                     }
	                  }
	               }
	            }
	         }
	      }

		Node temping = null;
		if (operand.car() instanceof ListNode && ((ListNode) operand.car()).car() instanceof FunctionNode) {
			temping = runList((ListNode) operand.cdr());
			operand = ListNode.cons(runList((ListNode) operand.car()), ListNode.cons(null, null));
		} // 중복된 경우에 대한 처리

		// 피연산자가 1개인 경우
		if (operand.cdr().car() == null && operand.car() instanceof IdNode) {
			Node temp = this.runExpr(operand.car());
			operand = ListNode.cons(temp, null);
		}

		switch (operator.value) { // CAR, CDR, CONS등에 대한 동작 구현

		case CAR: // CAR Function으로 operand의 car를 반환
			if (operand.car() instanceof QuoteNode) {
				operand = (ListNode) this.runQuote(operand);
				return operand.car();
			} else {
				errorLog("No Quote List");
				return null;
			}
		case CDR: // CDR Function으로 operand의 cdr을 반환
			if (operand.car() instanceof QuoteNode) {
				operand = (ListNode) this.runQuote(operand);
				return new QuoteNode(operand.cdr());
			} else {
				errorLog("No Quote List");
				return null;
			}
		case CONS: // cons연산으로 리스트를 합침
			Node head = operand.car();// head 값으로 operand.car()로 car의 부분을 받아온다
			Node tail = operand.cdr();

			head = this.runExpr(head);
			if (head instanceof QuoteNode) {
				head = ((QuoteNode) head).nodeInside();
			}
			if (operand.cdr().car() != null) {
				tail = this.runExpr(operand.cdr().car());
				tail = ((QuoteNode) tail).nodeInside();
			} else {
				tail = runQuote((ListNode) temping);
			}
			return new QuoteNode(ListNode.cons((Node) head, (ListNode) tail));

		case NULL_Q: // 리스트가 null list인지 확인
			if (operand.car() instanceof QuoteNode) {
				operand = (ListNode) runQuote(operand);
				if (runExpr(operand.car()) == null) {
					return BooleanNode.TRUE_NODE;
				} else {
					return BooleanNode.FALSE_NODE;
				}
			} else { // null?연산에 대해서 quote node가 아닌 겨우 예외처리
				errorLog("No Quote Node");
				return null;
			}

		case ATOM_Q: // quotenode인지 확인
			if (operand.car() instanceof QuoteNode) {
				QuoteNode tempcar = (QuoteNode) operand.car();
				if (tempcar.quoted instanceof ListNode) {
					return BooleanNode.FALSE_NODE;
				} else {
					return BooleanNode.TRUE_NODE;
				}
			} else { // atom?연산에 대해서 quote node가 아닌 경우 예외처리
				errorLog("No Quote Node");
				return null;
			}
		case EQ_Q: // list같은지 확인
			Node first = null;
			Node second = null;
			if (this.runExpr(operand.car()) instanceof QuoteNode) {
				first = ((QuoteNode) this.runExpr(operand.car())).nodeInside();
				second = ((QuoteNode) this.runExpr(operand.cdr().car())).nodeInside();
			} else {
				first = this.runExpr(operand.car());
				second = this.runExpr(operand.cdr().car());
			}
			if (first.toString().equals(second.toString())) {// 첫번째 값을 string으로
				// 바꾸어 그 값과
				// second 값을
				// toString을 통해
				// string으로 바꾸어
				// equals 로 비교하면
				return BooleanNode.TRUE_NODE;// 같으면 BooleanNode 값의 TRUE_NODE값을
				// 반환한다
			} else {// 다른 값이라면
				return BooleanNode.FALSE_NODE;// BooleanNode값의 FALSE_NODE 값을
				// 반환한다
			}

		case NOT:// NOT인 경우
			if (runExpr(operand.car()) instanceof BooleanNode) {
				if (runExpr(operand.car()).equals(BooleanNode.FALSE_NODE)) {// 만약
					// operand.car()
					// 부분값을
					// runExpr에
					// 넣어서
					// 리턴 받은
					// 값과
					// BooleanNode.TRUE_NODE 와 비교해서 같으면
					return BooleanNode.TRUE_NODE;// BooleanNode.TRUE_NODE 값을
													// return
					// 한다
				} else {// 만약 operand.car()부분을 runExpr에 넣어서 리터 받은 값이 TRUE_NODE 인
						// 경우는
					return BooleanNode.FALSE_NODE;// BooleanNode.FALSE_NODE 값을
					// return 한다
				}
			}
			else { // NOT연산 예외처리
				errorLog("NOT 연산은 BooleanNode에 대해서 연산이 가능합니다.");
				return null;
			}

		case COND: // 조건문
			if (operand.car() instanceof QuoteNode) {
				errorLog("Quote Node Can`t Be COND"); // 예외처리
				return null;
			} else {
				while (!operand.equals(ListNode.ENDLIST)) {// operand값이 ENDLIST가
					// 아닌동안 계속 while문을
					// 반복한다
					if (runExpr(((ListNode) operand.car()).car()).equals(BooleanNode.TRUE_NODE)) {
						return runExpr(((ListNode) operand.car()).cdr().car());
					} else {// TRUE가 아니라면 FALSE 이므로 다음 값을 조건 검사해서 판단 하기위해서
							// operand에 operand.cdr() 즉, 다음 값을 넣어서 다시 while문이
							// 돌아가게한다
						operand = operand.cdr();
					}
				}
			}
			errorLog("No True Condition");
			break;

		case DEFINE:
			if (operand.car() instanceof IdNode && operand.cdr().car() instanceof ListNode) {
				if ((((ListNode) (operand.cdr().car())).car()) instanceof FunctionNode) {
					if (((FunctionNode) (((ListNode) (operand.cdr().car())).car())).value
							.equals(FunctionNode.FunctionType.LAMBDA)) {
						insertTable((((IdNode) (operand.car())).getidStr()), (operand.cdr().car()));
						return BooleanNode.TRUE_NODE;
					}
				}
				insertTable((((IdNode) (operand.car())).getidStr()), runExpr(operand.cdr().car()));
				return BooleanNode.TRUE_NODE;
			} else if (operand.car() instanceof IdNode && operand.cdr().car() instanceof QuoteNode) {
				insertTable((((IdNode) (operand.car())).getidStr()), (operand.cdr().car()));
				return BooleanNode.TRUE_NODE;

			} else if (operand.car() instanceof IdNode) {
				insertTable((((IdNode) (operand.car())).getidStr()), runExpr(operand.cdr().car()));
				return BooleanNode.TRUE_NODE;
			} else {
				return BooleanNode.FALSE_NODE;
			}
		case LAMBDA: //case LAMBDA 일 때  
	         Node temp_Node = null; // temp_Node를 선언 
	         IdNode paramvalue = null; //paramvalue를 선언 
	         Node my_node = null;
	         if (operand instanceof ListNode) {//operand가 ListNode의 변수일 때 
	            ListNode insert_temp = (ListNode) operand.car();//operand의 car를 insert_temp에 저장하고 
	            while (insert_temp != null && temp != null) {//insert_temp와 temp 값이 모두 null이 아닌 동안 
	               if (insert_temp.car() instanceof IdNode) {//insert_temp의 car()부분이 IdNode일 때 
	                  {
	                     if (temp.car() instanceof IdNode) {//만약 temp.car 부분이 IdNode일 때는 그값을 새로 덮어 줘야하기 떄문에 
	                    	 
	                    	 if(lookupTable(temp.car()) instanceof QuoteNode){
	 	                         my_node = (QuoteNode) lookupTable(temp.car()); 
	                    	 }else{
	                         my_node = (IntNode) lookupTable(temp.car()); //temp.car부분을 lookUpTable로 찾아서 my_node에 저장한다 
	                    	 }
	                    	 paramvalue = (IdNode) insert_temp.car();//insert_temp.car을 paramvalue로 두고 
	                        if (defined_map.containsKey(paramvalue.getidStr())) {//defined_map을 이용해 paramvalue를 getString으로 바꾼 값을 이용해있는지 확인 하고 
	                           temp_Node = lookupTable(paramvalue);//paramvalue를 lookupTable을 이용해 찾아서 temp_Node에 저장한다 
	                        }
	                        insertTable(paramvalue.getidStr(), my_node);//또한 my_node부분을 paramvalue를 getString으로 바꾸고 그것을 table에 넣는다 
	                     }
	                  }
	                  if (temp.car() instanceof IntNode) {//만약 temp.car부분이 IntNode라면 
	                     IntNode actparam = (IntNode) temp.car();//temp의 car를 actparam에 저장하고 
	                     paramvalue = (IdNode) insert_temp.car();//insert_temp의 car를 paramvalue에 저장한다 
	                     if (defined_map.containsKey(paramvalue.getidStr())) {//paramvalue를 defined_map을 이용해 contain이 되어있는지 찾는다 
	                        temp_Node = lookupTable(paramvalue);//있다면 paramvalue 를 이용해 lookUpTable을 찾아서 temp_node에 저장한다 
	                     }
	                    
	                     insertTable(paramvalue.getidStr(), actparam);//paramvalue의 string값과 actparam을 이용해 table에 넣는다 
	                  }
	                  if( temp.car() instanceof QuoteNode){
	                	  QuoteNode actparam = (QuoteNode) temp.car();//temp의 car를 actparam에 저장하고 
		                     paramvalue = (IdNode) insert_temp.car();//insert_temp의 car를 paramvalue에 저장한다 
		                     if (defined_map.containsKey(paramvalue.getidStr())) {//paramvalue를 defined_map을 이용해 contain이 되어있는지 찾는다 
		                        temp_Node = lookupTable(paramvalue);//있다면 paramvalue 를 이용해 lookUpTable을 찾아서 temp_node에 저장한다 
		                     }
		                     insertTable(paramvalue.getidStr(), actparam);//paramvalue의 string값과 actparam을 이용해 table에 넣는다 
	                  }
	                  if(temp.car() instanceof ListNode){//temp.car 의 부분이 만약 ListNode라면 
	                        Node xxx = runExpr(temp.car());//temp의 car를 runExpr에 돌려서 그 값을 다시 xxx에 넣고 
	                        paramvalue =  (IdNode) ((ListNode)(temp.car())).cdr().car();//temp.car의 cdr의 car 값을 paramvalue로 두고 
	                        if (defined_map.containsKey(paramvalue.getidStr())) {//paramvalue를 defined_map을 이용해 contain이 되어있는지 찾는다 
	                              temp_Node = lookupTable(paramvalue);//있다면 paramvalue 를 이용해 lookUpTable을 찾아서 temp_node에 저장한다 
	                           }
	                        insertTable(paramvalue.getidStr(), xxx);//그리고 새로운 xxx를 재 정의한다 
	                     }
	               }
	         
	               insert_temp = insert_temp.cdr();//insert_temp를 다시 그 다음 값으로 주기위해 cdr을 insert_temp에 넣는다 
	               temp = temp.cdr();//temp를 다시 그 다음 값으로 주기위해 cdr를 temp에 넣는다 
	            }
	            ListNode func = (ListNode) operand.cdr().car();
	            Node result_node = runExpr(func);
	            if (temp_Node != null) {
	               insertTable(paramvalue.getidStr(), temp_Node); 
	                                               
	            } else {
	               if (paramvalue != null) {
	                  if (defined_map.containsKey(paramvalue.getidStr())) {
	                     defined_map.remove(paramvalue.getidStr());
	                                                    
	                  }
	               }
	            }

	            return result_node;

	         }
		default:
			break;

		}
		return null;

	}

	private Node runBinary(ListNode list) {
		BinaryOpNode operator = (BinaryOpNode) list.car(); // 구현과정에서 필요한 변수 및 함수

		int t1 = 0, t2 = 0; // binary연산에 필요한 피연산자 2개의 값 변수 선언
		if (list.cdr().car() == null || list.cdr().cdr().car() == null) {
			errorLog("피연산자 필요"); // 피연산자 갯수가 0개이거나 1개인 경우 예외처리
			return null;
		}
		if ((list.cdr().car()) instanceof IdNode) {
			// 피연산자 중 앞의 피연산자가 IdNode인 경우, 즉 앞의 피연산자가 table에 바인딩 된 값인 경우
			Node whatnumber = lookupTable((IdNode) list.cdr().car());
			// 테이블에서 바인딩된 값을 가져온다.
			if (whatnumber instanceof IntNode) { // 가져온 노드가 IntNode인 경우 int값을
													// 가져온다.
				t1 = ((IntNode) whatnumber).getvalue();
			}
		} else if (list.cdr().car() instanceof ListNode) {
			// 앞의 피연산자가 listNode인 경우
			if (((ListNode) list.cdr().car()).car() instanceof IdNode) {
				// List의 cdr의 car의 car이 IdNode인 경우에 대해서 ListNode의 cons를 사용 전역변수
				// temp와 cons한 것을 runExpr을 돌려서 나온 값을 저장
				Node runed_node = runExpr(ListNode.cons(((ListNode) list.cdr().car()).car(), temp));
				t1 = ((IntNode) runed_node).getvalue();
				// 위의 연산들을 통해서 plus2와 같은 연산을 할 때 피연산자에 plus1등 이 있는 경우에 대해서 연산이 된
				// 값을 가져온다.
			} else {
				//IdNode가 아닌 경우는 runBinary를 이용해서 계산된 값을 가져온다.
				//이 경우는 ListNode인 경우이므로 피연산자1이 ( + 2 3 ) 과 같은 List인 경우이다.
				t1 = ((IntNode) runExpr((ListNode) list.cdr().car())).getvalue();
			}
		} else {
			//위의 조건식에 안 걸리는 경우는 value를 가져온다.
			t1 = ((IntNode) list.cdr().car()).getvalue();
		}
		//두번쨰 피연산자 t2또한 t1과 같은 방식으로 값을 가져온다.
		if (list.cdr().cdr().car() instanceof IdNode) {
			//IdNode인 경우는 table을 이용해서 값을 가져오게 된다.
			Node whatnumber_2 = lookupTable((IdNode) list.cdr().cdr().car());
			//피연사자 부분이 IntNode인 경우
			if (whatnumber_2 instanceof IntNode) {
				//값을 가져온다.
				t2 = ((IntNode) whatnumber_2).getvalue();
			}
		} else if (list.cdr().cdr().car() instanceof ListNode) {
			//ListNode인 경우는 t1과 같은 방식으로 연산을 통해서 값을 가져온다.
			if (((ListNode) list.cdr().cdr().car()).car() instanceof IdNode) {
				Node runed_node = runExpr(ListNode.cons(((ListNode) list.cdr().cdr().car()).car(), temp));
				t2 = ((IntNode) runed_node).getvalue();
			} else {
				t2 = ((IntNode) runExpr((ListNode) list.cdr().cdr().car())).getvalue();
			}
		} else
			t2 = ((IntNode) list.cdr().cdr().car()).getvalue();
		//위의 과정을 통해서 t1, t2에는 피연산자 1,2의 값이 int값으로 저장이 되어있다.

		switch (operator.value) {
		case PLUS: // +연산
			return new IntNode(Integer.toString(t1 + t2));
		case MINUS: // -연산
			return new IntNode(Integer.toString(t1 - t2));
		case DIV: // 나누기 연산
			return new IntNode(Integer.toString(t1 / t2));
		case TIMES: // 곱하기 연산
			return new IntNode(Integer.toString(t1 * t2));
		case LT: // LT연산
			if (t1 < t2) {
				return BooleanNode.TRUE_NODE;
			} else {
				return BooleanNode.FALSE_NODE;
			}
		case GT: // GT연산
			if (t1 > t2) {
				return BooleanNode.TRUE_NODE;
			} else {
				return BooleanNode.FALSE_NODE;
			}
		case EQ: // =연산
			if (t1 == t2) {
				return BooleanNode.TRUE_NODE;
			} else {
				return BooleanNode.FALSE_NODE;
			}
		default:
			break;
		}
		return null;
	}

	private Node runQuote(ListNode node) {
		return ((QuoteNode) node.car()).nodeInside();
	}

}