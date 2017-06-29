package services

import javax.inject.Singleton

import jp.t2v.lab.play2.pager.scalikejdbc._
import jp.t2v.lab.play2.pager.{ Pager, SearchResult }
import models.User
import scalikejdbc.{ AutoSession, DBSession }

import scala.util.Try

@Singleton
class UserServiceImpl extends UserService {

  def create(user: User)(implicit dbSession: DBSession = AutoSession): Try[Long] = Try {
    User.create(user)
  }

  def findByEmail(email: String)(implicit dbSession: DBSession = AutoSession): Try[Option[User]] =
    Try {
      User.where('email -> email).apply().headOption
    }

  def findAll(pager: Pager[User])(implicit dbSession: DBSession): Try[SearchResult[User]] = Try {
    val size = User.countAllModels()
    SearchResult(pager, size) { pager =>
      User.findAllWithLimitOffset(
        pager.limit,
        pager.offset,
        pager.allSorters.map(_.toSQLSyntax(User.defaultAlias))
      )
    }
  }

  def findById(id: Long)(implicit dbSession: DBSession): Try[Option[User]] = Try {
    User.findById(id)
  }

}
