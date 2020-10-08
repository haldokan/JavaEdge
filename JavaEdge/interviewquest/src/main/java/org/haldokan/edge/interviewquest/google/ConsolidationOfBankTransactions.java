package org.haldokan.edge.interviewquest.google;

/**
 * My thoughts about a solution of this question: I first thought that it can be solved using a DAG but quickly realized
 * that it is actually *cyclic*. I think every cycle in the graph represents a transitive transaction that can be consolidated.
 * So the problem becomes finding all the cycles in the directed graph. I did some research and it turned out that a solution
 * for this problem is quite involved (search for D.B.Johnson simple cycles).
 *
 * It is hard to know if the intention of this question was to go in this direction. It is also possible (?) that a dynamic
 * programming recurrence can be used instead...
 *
 * todo: take up in the future if inspiration comes to town!
 *
 * There are multiple transactions from payee to payer. Consolidate all these transactions to minimum number of possible transactions.
 * HINT: Consolidate transitive transactions along with similar transactions
 *
 * Example:
 * transactions = [
 * {"payee": "BoA", "amount": 132, "payer": "Chase"},
 * {"payee": "BoA", "amount": 827, "payer": "Chase"},
 * {"payee": "Well Fargo", "amount": 751, "payer": "BoA"},
 * {"payee": "BoA", "amount": 585, "payer": "Chase"},
 * {"payee": "Chase", "amount": 877, "payer": "Well Fargo"},
 * {"payee": "Well Fargo", "amount": 157, "payer": "Chase"},
 * {"payee": "Well Fargo", "amount": 904, "payer": "Chase"},
 * {"payee": "Chase", "amount": 976, "payer": "BoA"},
 * {"payee": "Chase", "amount": 548, "payer": "Well Fargo"},
 * {"payee": "BoA", "amount": 872, "payer": "Well Fargo"}]
 */
public class ConsolidationOfBankTransactions {
}
