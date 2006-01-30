<jsp:directive.include file="includes/top.jsp" />


	<form method="post" action="">
	
		<spring:hasBindErrors name="credentials">
			<div id="errors">
				<ul>
		  <c:forEach var="error" items="${errors.allErrors}">
		      <li><spring:message code="${error.code}" text="${error.defaultMessage}" /></li>
		  </c:forEach>
		  </ul>
		  </div>
		</spring:hasBindErrors>
	
		<div id="welcome" width="100%">
			<div style="margin-left: auto; margin-right: auto; left: 50%">
			<p>Congratulations on bringing CAS online!  The default authentication handler authenticates where usernames equal passwords: go ahead, try it out. </p>
			<p>For security reasons, please Log Out and Exit your web browser when you are done accessing services that require authentication!</p>
			

			
			
				<p><strong>Enter your JA-SIG NetID and Password.</strong></p>
				<p>
					<label for="username"><span class="accesskey">N</span>etID:</label><br />
					<input class="required" id="username" name="username" size="32" tabindex="1" accesskey="n" />
				</p>

				<p>
					<label for="password"><span class="accesskey">P</span>assword:</label><br />

					<input class="required" type="password" id="password" name="password" size="32" tabindex="2" accesskey="p" />
				</p>

				<p><input style="width:1.5em;border:0;padding:0;margin:0;" type="checkbox" id="warn" name="warn" value="true" tabindex="3" /> 
				   <label for="warn"  accesskey="w"><span class="accesskey">W</span>arn me before logging me into other sites.</label></p>

				<input type="hidden" name="lt" value="${flowExecutionId}" />
				<input type="hidden" name="_currentStateId" value="${currentStateId}" />
				<input type="hidden" name="_eventId" value="submit" />

				<p><input type="submit" class="button" accesskey="l" value="LOGIN" tabindex="4" />
				   <input type="reset" class="button" accesskey="c" value="CLEAR" tabindex="5" /></p>
			</div>
		</div>
	</form>
<jsp:directive.include file="includes/bottom.jsp" />