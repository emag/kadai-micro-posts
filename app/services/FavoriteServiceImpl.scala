package services

import javax.inject.Singleton

import jp.t2v.lab.play2.pager.{ OrderType, Pager, SearchResult, Sorter }
import models.{ Favorite, MicroPost }
import scalikejdbc._

import scala.util.Try

@Singleton
class FavoriteServiceImpl extends FavoriteService {

  implicit def sortersToSQLSyntaxs(sorters: Seq[Sorter[MicroPost]]): Seq[SQLSyntax] = {
    sorters.map { sorter =>
      if (sorter.dir == OrderType.Descending)
        Favorite.defaultAlias.id.desc
      else
        Favorite.defaultAlias.id.asc
    }
  }

  override def create(favorite: Favorite)(implicit dbSession: DBSession): Try[Long] = Try {
    Favorite.create(favorite)
  }

  override def findById(userId: Long)(implicit dbSession: DBSession): Try[List[Favorite]] = Try {
    Favorite.where('userId -> userId).apply()
  }

  /**
    * userId のユーザのお気に入りのポストの集合を取得する
    */
  override def findByUserId(pager: Pager[MicroPost], userId: Long)(
      implicit dbSession: DBSession
  ): Try[SearchResult[MicroPost]] = {
    countByUserId(userId).map { size =>
      SearchResult(pager, size) { pager =>
        Favorite.allAssociations
          .findAllByWithLimitOffset(
            sqls.eq(Favorite.defaultAlias.userId, userId),
            pager.limit,
            pager.offset,
            pager.allSorters
          )
          .map(_.microPost.get)
      }
    }
  }

  override def countByUserId(userId: Long)(implicit dbSession: DBSession): Try[Long] = Try {
    Favorite.allAssociations.countBy(sqls.eq(Favorite.defaultAlias.userId, userId))
  }

  override def deleteBy(userId: Long, microPostId: Long)(implicit dbSession: DBSession): Try[Int] = Try {
    val c     = Favorite.column
    val count = Favorite.countBy(sqls.eq(c.userId, userId).and.eq(c.microPostId, microPostId))
    if (count == 1) {
      Favorite.deleteBy(
        sqls
          .eq(Favorite.column.userId, userId)
          .and(sqls.eq(Favorite.column.microPostId, microPostId))
      )
    } else 0
  }
}
