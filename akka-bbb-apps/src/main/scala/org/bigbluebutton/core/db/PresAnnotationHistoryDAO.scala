package org.bigbluebutton.core.db

import org.bigbluebutton.common2.msgs.AnnotationVO
import PostgresProfile.api._

case class PresAnnotationHistoryDbModel(
    sequence:       Option[Int] = None,
    annotationId:   String,
    pageId:         String,
    meetingId:      String,
    userId:         String,
    annotationInfo: String
//    lastUpdatedAt:  java.sql.Timestamp = new java.sql.Timestamp(System.currentTimeMillis())
)

class PresAnnotationHistoryDbTableDef(tag: Tag) extends Table[PresAnnotationHistoryDbModel](tag, None, "pres_annotation_history") {
  val sequence = column[Option[Int]]("sequence", O.PrimaryKey, O.AutoInc)
  val annotationId = column[String]("annotationId")
  val pageId = column[String]("pageId")
  val meetingId = column[String]("meetingId")
  val userId = column[String]("userId")
  val annotationInfo = column[String]("annotationInfo")
  //  val lastUpdatedAt = column[java.sql.Timestamp]("lastUpdatedAt")
  //  def whiteboard = foreignKey("whiteboard_fk", whiteboardId, Whiteboards)(_.whiteboardId, onDelete = ForeignKeyAction.Cascade)
  def * = (sequence, annotationId, pageId, meetingId, userId, annotationInfo) <> (PresAnnotationHistoryDbModel.tupled, PresAnnotationHistoryDbModel.unapply)
}

object PresAnnotationHistoryDAO {

  def insert(meetingId: String, annotationDiff: AnnotationVO) = {
    DatabaseConnection.db.run(
      //TODO not being used for now
      TableQuery[PresAnnotationHistoryDbTableDef].returning(
        TableQuery[PresAnnotationHistoryDbTableDef].map(_.sequence)
      ) += PresAnnotationHistoryDbModel(
          None,
          annotationId = annotationDiff.id,
          pageId = annotationDiff.wbId,
          meetingId = meetingId,
          userId = annotationDiff.userId,
          annotationInfo = JsonUtils.mapToJson(annotationDiff.annotationInfo).compactPrint
        )
    )
  }

  def delete(wbId: String, meetingId: String, userId: String, annotationId: String) = {
    DatabaseConnection.db.run(
      //TODO not being used for now
      TableQuery[PresAnnotationHistoryDbTableDef].returning(
        TableQuery[PresAnnotationHistoryDbTableDef].map(_.sequence)
      ) += PresAnnotationHistoryDbModel(
          None,
          annotationId = annotationId,
          pageId = wbId,
          meetingId = meetingId,
          userId = userId,
          annotationInfo = ""
        )
    )
  }
}