package id.kibar.api.data.persistence

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.io.FileReader
import java.sql.Connection
import java.util.*

object DbPool {

    private val hikariDataSource : HikariDataSource

    init {
        val props = Properties()
        props.load(FileReader("db/flyway.conf"))

        val config = HikariConfig()

        config.jdbcUrl = props.getProperty("flyway.url")
        config.username = props.getProperty("flyway.user")
        config.password = props.getProperty("flyway.password")
        // These configs are based on: https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
        config.addDataSourceProperty("cachePrepStmts", "true")
        config.addDataSourceProperty("prepStmtCacheSize", "250")
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        config.addDataSourceProperty("useServerPrepStmts", "true")
        config.addDataSourceProperty("useLocalSessionState", "true")
        config.addDataSourceProperty("useLocalTransactionState", "true")
        config.addDataSourceProperty("rewriteBatchedStatements", "true")
        config.addDataSourceProperty("cacheResultSetMetadata", "true")
        config.addDataSourceProperty("cacheServerConfiguration", "true")
        config.addDataSourceProperty("elideSetAutoCommits", "true")
        config.addDataSourceProperty("maintainTimeStats", "false")
        config.addDataSourceProperty("maxLifetime", "60000")


        hikariDataSource = HikariDataSource(config)
    }

    fun getConnection(): Connection {
        return hikariDataSource.connection
    }
}