# kadai-micro-posts

マイクロポスト Twitterクローン

## 前提条件

* MySQL 5.7.x を立ち上げておくこと
  * 5.7.17 で動作確認
  * ローカルの MySQL に対するデータベースの作成は、必要に応じて `create-mysql-db.sql` を修正した上で以下を実行する
    * `sh ./create-local-mysql-db.sh`
* env/dev.conf を適宜修正すること(特に `jdbcPassword`)

## 実行

``` console
sbt flywayMigrate
sbt run
```
