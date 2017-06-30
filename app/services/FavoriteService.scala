package services

import jp.t2v.lab.play2.pager.{ Pager, SearchResult }
import models.{ Favorite, MicroPost, User }
import scalikejdbc.{ AutoSession, DBSession }

import scala.util.Try

trait FavoriteService {

  def create(favorite: Favorite)(implicit dbSession: DBSession = AutoSession): Try[Long]

  def findById(userId: Long)(implicit dbSession: DBSession = AutoSession): Try[List[Favorite]]

  def findByUserId(pager: Pager[MicroPost], userId: Long)(
      implicit dbSession: DBSession = AutoSession
  ): Try[SearchResult[MicroPost]]

  def countByUserId(userId: Long)(implicit dbSession: DBSession = AutoSession): Try[Long]

  def deleteBy(userId: Long, microPostId: Long)(implicit dbSession: DBSession = AutoSession): Try[Int]

}
