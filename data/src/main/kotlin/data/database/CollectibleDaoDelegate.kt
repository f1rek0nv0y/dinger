package data.database

internal abstract class CollectibleDaoDelegate<in PrimaryKey, Resolved>
  : DaoDelegate<PrimaryKey, Resolved>() {
  fun collectByPrimaryKeys(primaryKeys: Iterable<PrimaryKey>): Iterable<Resolved> =
      primaryKeys.fold(emptySet(), { acc, s -> acc.plus(selectByPrimaryKey(s)) })
}
