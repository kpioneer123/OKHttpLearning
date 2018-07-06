package com.haocai.download.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;



// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig downloadEntityDaoConfig;

    private final DownloadEntityDao downloadEntityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        downloadEntityDaoConfig = daoConfigMap.get(DownloadEntityDao.class).clone();
        downloadEntityDaoConfig.initIdentityScope(type);

        downloadEntityDao = new DownloadEntityDao(downloadEntityDaoConfig, this);

        registerDao(DownloadEntity.class, downloadEntityDao);
    }
    
    public void clear() {
        downloadEntityDaoConfig.clearIdentityScope();
    }

    public DownloadEntityDao getDownloadEntityDao() {
        return downloadEntityDao;
    }

}