package com.quantweb.mdserver.table

import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import com.quantweb.mdserver.table.TableDataServer.UpdateTableDataRow

//YAGNI! - If the below two traits are ever needed, then split them out as more generic base traits
//trait SenderEngine {
//  type MessageType
//	def send(message: MessageType)
//}
//
//trait Sender{
//    val senderEngine: SenderEngine
//}

//Single target sender
trait TableDataSenderEngine{
    type MessageType
	def send(message: MessageType)
}

//Single target sender
//Akka Actor-based sender, although Sender doesn't need to be an instance of Actor 
trait TableDataSender{
    //Instead of directly injecting senderEngine, inject targetRef which tells how to craete senderEngine to inject
    val targetRef: ActorRef

    //It's giving the default implementation of the senderEngine, but you can extend this trait to override senderEngine to inject a different one 
    val senderEngine = new TableDataSenderEngine{
        type MessageType = TableDataRow
    	override def send(row: MessageType) = targetRef ! UpdateTableDataRow( row ) 
	}
}