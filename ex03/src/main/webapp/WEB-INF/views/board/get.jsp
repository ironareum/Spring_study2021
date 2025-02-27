<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<%@ include file ="../includes/header.jsp" %>
			
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Board Register</h1>
                </div>
                <!-- /. col-lg-12 -->
            </div>
            <!-- /. row -->
            
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                    
                        <div class="panel-heading">Board Register</div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                        	
                        		<div class="form-group">
                        			<label for="bno">Bno</label>
                        			<input class="form-control" name="bno" 
                        			value='<c:out value="${board.bno}"/>' readonly="readonly"/>
                        		</div>
                        		
                        		<div class="form-group">
                        			<label for="title">Title</label>
                        			<input class="form-control" name="title"
                        			value='<c:out value="${board.title}"/>' readonly="readonly"/>
                        		</div>
                        		
                        		<div class="form-group">
                        			<label for="content">Text area</label>
                        			<textarea class="form-control" rows="3" name="content"
                        			 readonly="readonly"><c:out value="${board.content}"/></textarea>
                        		</div>

                        		<div class="form-group">
                        			<label for="writer">Writer</label>
                        			<input class="form-control" name="writer"
                        			value='<c:out value="${board.writer}"/>' readonly="readonly"/>
                        		</div>
                        		
                        		<button data-oper="modify" class="btn btn-default">Modify</button>
                        		
                        		<button data-oper="list" class="btn btn-info">List</button>
                            	
                            	<form id="operForm" action="/board/modify" method="get">
	                            	<input type="hidden" id="bno" name="bno" value="<c:out value='${board.bno}'/>" />
	                            	<input type="hidden" name="pageNum" value="<c:out value='${cri.pageNum}'/>"/>
	                            	<input type="hidden" name="amount" value="<c:out value='${cri.amount}'/>"/>
	                            	<input type="hidden" name="type" value="<c:out value='${cri.type}'/>"/>
	                            	<input type="hidden" name="keyword" value="<c:out value='${cri.keyword}'/>"/>
                            	</form>
                        </div>
                        <!-- (e) panel-body -->
                    </div>
                    <!-- (e) panel -->
                </div>
                <!-- (e) col-lg-6 -->
            </div>
            <!-- (e) row -->
        	
        	
        	<!-- 댓글 (s) -->
        	<div class="row">
        		<div class="col-lg-12">
        			<!-- .panel (s) -->
        			<div class="panel panel-default">
        				
        				<!-- panel heading -->
        				<div class="panel-heading">
        					<i class="fa fa-comments fa-fw"></i> Reply
        				</div>
        				
        				<!-- panel body -->
        				<div class="panel-body">
        					<ul class="chat">
        						
        						<!-- start reply -->
        						<li class="left clearfix" data-rno='12'>
        							<div>
        								<div class="header">
        									<strong class="primary-font">user00</strong>
        									<small class="pull-right text-muted">2018-01-01 13:13</small>
        								</div>
        								<p>Good job!</p>
        							</div>
        						</li>
        						<!-- end reply -->
        					</ul>
        					<!-- end ul -->
        				</div>
        				<!-- .panel .chat-panel (e) -->
        				
        			</div>
        		</div>
        	</div>
        	<!-- 댓글 (e) -->
        	
        	
<%@ include file="../includes/footer.jsp" %>



<!-- reply 관련 스트립트  -->
<script type="text/javascript" src="/resources/js/reply.js"></script>
<script>
	$(document).ready(function(){
		
		var bnoValue = '<c:out value="${board.bno}"/>';
		var replyUL = $(".chat");
		
		/* showList(1);
		
		function showList(page){
			replyService.getList({bno:bnoValue, page: page || 1}, function(list){
				
				var str="";
				if(list == null || list.length == 0){

					replyUL.html("");
					return;
					
				}
				for(var i = 0, len = list.length || 0; i < len; i++ ){
					str += "<li class='left clearfix' data-rno='" + list[i].rno + "'>";
					str += "	<div><div class='header'><strong class='primary-font'>" + list[i].replyer + "</strong>";
					str += "		<small class='pull-right text-muted'>" + replyService.displayTime(list[i].replyDate) + "</small>";
					str += "</div><p>" + list[i].reply + "</p></div></li>";
				}
				
				replyUL.html(str);
			});
		}
		 */		
	});
	
	/********************** 
	* ajax 댓글처리 테스트 (s) * 
	***********************/
	console.log("=========");
	console.log("JS TEST");
	
	var bnoValue = '<c:out value="${board.bno}"/>';
	console.log("bno: ", bnoValue);
	
	/* 댓글 삽입 */
	replyService.add(
		{reply:"JS TEST", replyer: "tester", bno: bnoValue},
		function(result){
			alert("RESULT: "+ result);
		},
		function(err){
			alert("error: insert");
		});
	
	/*  댓글 리스트 조회 */	
	replyService.getList({bno:bnoValue, page: 1}, function(list){
		for(var i = 0, len = list.length || 0; i < len; i++ ){
			console.log(list[i]);
		}
	});
	
	/* 댓글 삭제 */
	replyService.remove(24, function(result){
		console.log(result);
		if(result === 'success'){
			alert("Removed");
		}
	}, function(err){
		alert("Error : remove");
	}); 
	
	/* 댓글 수정 */
	replyService.update({rno: 54, bno: bnoValue, reply: "수정되고 있어??????????????????"}
	, function(result){
		console.log("update result: "+ result);
		alert("수정 완료..." + result ); }
	, function(err){
		alert("Error : modify");
	});
	
	/* 단일 댓글 조회 */
	replyService.get(42, function(data){
		console.log("단일댓글 data: ",data);
	});

</script>
<script type="text/javascript">
	$(document).ready(function(){
		
		var operForm = $("#operForm");
		
		$("button[data-oper='modify']").on("click", function(e){
			operForm.attr("action", "/board/modify").submit();
		});
		
		$("button[data-oper='list']").on("click", function(e){
			operForm.find("#bno").remove();
			operForm.attr("action", "/board/list");
			operForm.submit();
		});
	});
</script>