package org.haldokan.edge.guava;

import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

public enum GuavaFunnel implements Funnel<Account> {
    INSTANCE;

    @Override
    public void funnel(Account from, PrimitiveSink into) {
	into.putUnencodedChars(from.getId()).putUnencodedChars(from.getOwner()).putDouble(from.getBalance());
    }

}
