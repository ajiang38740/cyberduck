package ch.cyberduck.core.vault.registry;

/*
 * Copyright (c) 2002-2017 iterate GmbH. All rights reserved.
 * https://cyberduck.io/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

import ch.cyberduck.core.AttributedList;
import ch.cyberduck.core.Cache;
import ch.cyberduck.core.Filter;
import ch.cyberduck.core.ListProgressListener;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.Session;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.features.Search;
import ch.cyberduck.core.vault.VaultRegistry;

public class VaultRegistrySearchFeature implements Search {

    private final Session<?> session;
    private final Search proxy;
    private final VaultRegistry registry;

    private Cache<Path> cache;

    public VaultRegistrySearchFeature(final Session<?> session, final Search proxy, final VaultRegistry registry) {
        this.session = session;
        this.proxy = proxy;
        this.registry = registry;
    }

    @Override
    public AttributedList<Path> search(final Path workdir, final Filter<Path> regex, final ListProgressListener listener) throws BackgroundException {
        return registry.find(session, workdir).getFeature(session, Search.class, proxy)
                .withCache(cache)
                .search(workdir, regex, listener);
    }

    @Override
    public boolean isRecursive() {
        return proxy.isRecursive();
    }

    @Override
    public Search withCache(final Cache<Path> cache) {
        this.cache = cache;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VaultRegistrySearchFeature{");
        sb.append("proxy=").append(proxy);
        sb.append('}');
        return sb.toString();
    }
}
