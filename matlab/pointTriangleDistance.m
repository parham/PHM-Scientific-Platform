






  


  
  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
  <head>
      <meta http-equiv="X-UA-Compatible" content="IE=8"/>
      
      <!-- START OF GLOBAL NAV -->
  <link rel="stylesheet" href="/matlabcentral/css/sitewide.css" type="text/css">
  <link rel="stylesheet" href="/matlabcentral/css/mlc.css" type="text/css">
  <!--[if lt IE 7]>
  <link href="/matlabcentral/css/ie6down.css" type="text/css" rel="stylesheet">
  <![endif]-->

      
      <meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta name="keywords" content="file exchange, matlab answers, newsgroup access, link exchange, matlab blog, matlab central, simulink blog, matlab community, matlab and simulink community">
<meta name="description" content="File exchange, MATLAB Answers, newsgroup access, Links, and Blogs for the MATLAB &amp; Simulink user community">
<link rel="stylesheet" href="/matlabcentral/css/fileexchange.css" type="text/css">
<link rel="stylesheet" type="text/css" media="print" href="/matlabcentral/css/print.css" />
<title>Distance between a point and a triangle in 3D: pointTriangleDistance(TRI,P) -  File Exchange - MATLAB Central</title>
<script src="/matlabcentral/fileexchange/javascripts/jquery-1.9.1.min.js?1368026756" type="text/javascript"></script>
<script src="/matlabcentral/fileexchange/javascripts/jquery-ui-1.10.1.min.js?1368026756" type="text/javascript"></script>
<script src="/matlabcentral/fileexchange/javascripts/searchfield.js?1368026756" type="text/javascript"></script>
<script src="/matlabcentral/fileexchange/javascripts/jquery_ujs.js?1368026756" type="text/javascript"></script>
<script src="/matlabcentral/fileexchange/javascripts/application.js?1368026756" type="text/javascript"></script>
<link href="/matlabcentral/fileexchange/stylesheets/application.css?1368026756" media="screen" rel="stylesheet" type="text/css" />
<link href="/matlabcentral/fileexchange/stylesheets/jquery-ui.css?1368026756" media="screen" rel="stylesheet" type="text/css" />
<link href="http://www.mathworks.com/matlabcentral/fileexchange/22857-distance-between-a-point-and-a-triangle-in-3d/content/pointTriangleDistance.m" rel="canonical" />
<link rel="search" type="application/opensearchdescription+xml" title="Search File Exchange" href="/matlabcentral/fileexchange/search.xml" />


  </head>
    <body>
      <div id="header">
  <div class="wrapper">
  <!--put nothing in left div - only 11px wide shadow --> 
    <div class="main">
        	<div id="logo"><a href="/matlabcentral/" title="MATLAB Central Home"><img src="/matlabcentral/images/mlclogo-whitebgd.gif" alt="MATLAB Central" /></a></div>
      
        <div id="headertools">
        

<script language="JavaScript1.3" type="text/javascript">

function submitForm(query){

	choice = document.forms['searchForm'].elements['search_submit'].value;
	
	if (choice == "entire1" || choice == "contest" || choice == "matlabcentral" || choice == "blogs"){
	
	   var newElem = document.createElement("input");
	   newElem.type = "hidden";
	   newElem.name = "q";
	   newElem.value = query.value;
	   document.forms['searchForm'].appendChild(newElem);
	      
	   submit_action = '/searchresults/';
	}
	
	switch(choice){
	   case "matlabcentral":
	      var newElem = document.createElement("input");
	      newElem.type = "hidden";
	      newElem.name = "c[]";
	      newElem.value = "matlabcentral";
	      document.forms['searchForm'].appendChild(newElem);
	
	      selected_index = 0;
	      break
	   case "fileexchange":
	      var newElem = document.createElement("input");
	      newElem.type = "hidden";
	      newElem.name = "term";
	      newElem.value = query.value;
	      newElem.classname = "formelem";
	      document.forms['searchForm'].appendChild(newElem);
	
	      submit_action = "/matlabcentral/fileexchange/";
	      selected_index = 1;
	      break
	   case "answers":
	      var newElem = document.createElement("input");
	      newElem.type = "hidden";
	      newElem.name = "term";
	      newElem.value = query.value;
	      newElem.classname = "formelem";
	      document.forms['searchForm'].appendChild(newElem);
	
	      submit_action = "/matlabcentral/answers/";
	      selected_index = 2;
	      break
	   case "cssm":
	      var newElem = document.createElement("input");
	      newElem.type = "hidden";
	      newElem.name = "search_string";
	      newElem.value = query.value;
	      newElem.classname = "formelem";
	      document.forms['searchForm'].appendChild(newElem);
	
		  submit_action = "/matlabcentral/newsreader/search_results";
	      selected_index = 3;
	      break
	   case "linkexchange":
	      var newElem = document.createElement("input");
	      newElem.type = "hidden";
	      newElem.name = "term";
	      newElem.value = query.value;
	      newElem.classname = "formelem";
	      document.forms['searchForm'].appendChild(newElem);
	
	      submit_action = "/matlabcentral/linkexchange/";
	      selected_index = 4;
	      break
	   case "blogs":
	      var newElem = document.createElement("input");
	      newElem.type = "hidden";
	      newElem.name = "c[]";
	      newElem.value = "blogs";
	      document.forms['searchForm'].appendChild(newElem);
	
	      selected_index = 5;
	      break
	   case "trendy":
	      var newElem = document.createElement("input");
	      newElem.type = "hidden";
	      newElem.name = "search";
	      newElem.value = query.value;
	      newElem.classname = "formelem";
	      document.forms['searchForm'].appendChild(newElem);
	
	      submit_action = "/matlabcentral/trendy";
	      selected_index = 6;
	      break
	   case "cody":
	      var newElem = document.createElement("input");
	      newElem.type = "hidden";
	      newElem.name = "term";
	      newElem.value = query.value;
	      newElem.classname = "formelem";
	      document.forms['searchForm'].appendChild(newElem);
	
	      submit_action = "/matlabcentral/cody/";
	      selected_index = 7;
	      break
	   case "contest":
	      var newElem = document.createElement("input");
	      newElem.type = "hidden";
	      newElem.name = "c[]";
	      newElem.value = "contest";
	      document.forms['searchForm'].appendChild(newElem);
	
	      selected_index = 8;
	      break
	   case "entire1":
	      var newElem = document.createElement("input");
	      newElem.type = "hidden";
	      newElem.name = "c[]";
	      newElem.value = "entiresite";
	      document.forms['searchForm'].appendChild(newElem);
	      
	      selected_index = 9;
	      break
	   default:
	      var newElem = document.createElement("input");
	      newElem.type = "hidden";
	      newElem.name = "c[]";
	      newElem.value = "entiresite";
	      document.forms['searchForm'].appendChild(newElem);
	   
	      selected_index = 9;
	      break
	}

	document.forms['searchForm'].elements['search_submit'].selectedIndex = selected_index;
	document.forms['searchForm'].elements['query'].value = query.value;
	document.forms['searchForm'].action = submit_action;
}

</SCRIPT>


  <form name="searchForm" method="GET" action="" style="margin:0px; margin-top:5px; font-size:90%" onSubmit="submitForm(query)">
          <label for="search">Search: </label>
        <select name="search_submit" style="font-size:9px ">
         	 <option value = "matlabcentral">MATLAB Central</option>
          	<option value = "fileexchange" selected>&nbsp;&nbsp;&nbsp;File Exchange</option>
          	<option value = "answers">&nbsp;&nbsp;&nbsp;Answers</option>
            <option value = "cssm">&nbsp;&nbsp;&nbsp;Newsgroup</option>
          	<option value = "linkexchange">&nbsp;&nbsp;&nbsp;Link Exchange</option>
          	<option value = "blogs">&nbsp;&nbsp;&nbsp;Blogs</option>
          	<option value = "trendy">&nbsp;&nbsp;&nbsp;Trendy</option>
          	<option value = "cody">&nbsp;&nbsp;&nbsp;Cody</option>
          	<option value = "contest">&nbsp;&nbsp;&nbsp;Contest</option>
          <option value = "entire1">MathWorks.com</option>
        </select>
<input type="text" name="query" size="10" class="formelem" value="">
<input type="submit" value="Go" class="formelem gobutton" >
</form>

		  <ol id="access2">
  <li class="first">
    <a href="https://www.mathworks.com/accesslogin/createProfile.do?uri=http%3A%2F%2Fwww.mathworks.com%2Fmatlabcentral%2Ffileexchange%2F22857-distance-between-a-point-and-a-triangle-in-3d%2Fcontent%2FpointTriangleDistance.m" id="create_account_link" rel="nofollow">Create Account</a>
  </li>
  <li>
    <a href="https://www.mathworks.com/accesslogin/index_fe.do?uri=http%3A%2F%2Fwww.mathworks.com%2Fmatlabcentral%2Ffileexchange%2F22857-distance-between-a-point-and-a-triangle-in-3d%2Fcontent%2FpointTriangleDistance.m" id="login_link" rel="nofollow">Log In</a>
  </li>
</ol>


      </div>
	  
        <div id="globalnav">
        <!-- from includes/global_nav.html -->
        <ol>
                <li class=";" >
                        <a href="/matlabcentral/fileexchange/">File Exchange</a> 
                </li>
                <li class=";" >
                        <a href="/matlabcentral/answers/">Answers</a> 
                </li>
                <li class=";" >
                        <a href="/matlabcentral/newsreader/">Newsgroup</a> 
                </li>
                <li class=";" >
                        <a href="/matlabcentral/linkexchange/">Link Exchange</a> 
                </li>
                <li class=";" >
                        <a href="http://blogs.mathworks.com/">Blogs</a> 
                </li>
                <li class=";" >
                        <a href="/matlabcentral/trendy">Trendy</a> 
                </li>
                <li class=";" >
                        <a href="/matlabcentral/cody">Cody</a> 
                </li>
                <li class=";" >
                        <a href="/matlabcentral/contest/">Contest</a> 
                </li>
                <li class="icon mathworks" >
                        <a href="/">MathWorks.com</a> 
                </li>
        </ol>
      </div>
    </div>
  </div>
</div>

      <div id="middle">
  <div class="wrapper">

    <div id="mainbody" class="columns2">

  <div class="manifest">
    <div class="ctaBtn ctaBlueBtn btnSmall">
            <div id="download_submission_button" class="btnCont">
              <div class="btn download"><a href="http://www.mathworks.com/matlabcentral/fileexchange/22857-distance-between-a-point-and-a-triangle-in-3d?download=true" title="Download Now">Download Submission</a></div>
            </div>
          </div>


  <p class="license">
      Code covered by the <a href="/matlabcentral/fileexchange/view_license?file_info_id=22857" popup="new_window height=500,width=640,scrollbars=yes">BSD License</a>
      <a href="/matlabcentral/fileexchange/help_license#bsd" class="info notext" onclick="window.open(this.href,'small','toolbar=no,resizable=yes,status=yes,menu=no,scrollbars=yes,width=600,height=550');return false;">&nbsp;</a>
  </p>


    <h3 class="highlights_title">
      Highlights from<br/>
      <a href="http://www.mathworks.com/matlabcentral/fileexchange/22857-distance-between-a-point-and-a-triangle-in-3d" class="manifest_title">Distance between a point and a triangle in 3D</a>
    </h3>


    <ul class="manifest">
          <li class="manifest">
              <a href="http://www.mathworks.com/matlabcentral/fileexchange/22857-distance-between-a-point-and-a-triangle-in-3d/content/pointTriangleDistance.m" class="function" title="Function">pointTriangleDistance(TRI,P)</a>
            <span class="description">calculate distance between a point and a triangle in 3D</span>
          </li>
      <li class="manifest_allfiles">
        <a href="http://www.mathworks.com/matlabcentral/fileexchange/22857-distance-between-a-point-and-a-triangle-in-3d/all_files">View all files</a></li>
    </ul>
</div>


  <table cellpadding="0" cellspacing="0" class="details file">
    <tr>
      <th class="maininfo">

          <div id="thumbnail">
            <img src="/matlabcentral/fx_files/22857/7/thumbnail.jpg" alt="image thumbnail">
          </div>
        <div id="details" class="content_type_details">
          from
          <a href="/matlabcentral/fileexchange/file_infos/22857-distance-between-a-point-and-a-triangle-in-3d" class="content_type_author">Distance between a point and a triangle in 3D</a></h2>
          by <a href="http://www.mathworks.com/matlabcentral/fileexchange/authors/31958">Gwendolyn Fischer</a>
          <br/>pointTriangleDistance calculates the distance between a point and a triangle in 3D.
          </p>
        </div>
      </th>
    </tr>
    <tr>
      <td class="file">
        <table cellpadding="0" cellspacing="0" border="0" class="fileview section">
          <tr class="title">
            <th><span class="heading">pointTriangleDistance(TRI,P)</span></th>
          </tr>
          <tr>
            <td>
                <div class="codecontainer"><pre class="matlab-code">function [dist,PP0] = pointTriangleDistance(TRI,P)
% calculate distance between a point and a triangle in 3D
% SYNTAX
%   dist = pointTriangleDistance(TRI,P)
%   [dist,PP0] = pointTriangleDistance(TRI,P)
%
% DESCRIPTION
%   Calculate the distance of a given point P from a triangle TRI.
%   Point P is a row vector of the form 1x3. The triangle is a matrix
%   formed by three rows of points TRI = [P1;P2;P3] each of size 1x3.
%   dist = pointTriangleDistance(TRI,P) returns the distance of the point P
%   to the triangle TRI.
%   [dist,PP0] = pointTriangleDistance(TRI,P) additionally returns the
%   closest point PP0 to P on the triangle TRI.
%
% Author: Gwendolyn Fischer
% Release: 1.0
% Release date: 09/02/02
% Release: 1.1 Fixed Bug because of normalization
% Release: 1.2 Fixed Bug because of typo in region 5 20101013
% Release: 1.3 Fixed Bug because of typo in region 2 20101014

% Possible extention could be a version tailored not to return the distance
% and additionally the closest point, but instead return only the closest
% point. Could lead to a small speed gain.

% Example:
% %% The Problem
% P0 = [0.5 -0.3 0.5];
% 
% P1 = [0 -1 0];
% P2 = [1  0 0];
% P3 = [0  0 0];
% 
% vertices = [P1; P2; P3];
% faces = [1 2 3];
% 
% %% The Engine
% [dist,PP0] = pointTriangleDistance([P1;P2;P3],P0);
%
% %% Visualization
% [x,y,z] = sphere(20);
% x = dist*x+P0(1);
% y = dist*y+P0(2);
% z = dist*z+P0(3);
% 
% figure
% hold all
% patch('Vertices',vertices,'Faces',faces,'FaceColor','r','FaceAlpha',0.8);
% plot3(P0(1),P0(2),P0(3),'b*');
% plot3(PP0(1),PP0(2),PP0(3),'*g')
% surf(x,y,z,'FaceColor','b','FaceAlpha',0.3)
% view(3)

% The algorithm is based on 
% &quot;David Eberly, 'Distance Between Point and Triangle in 3D',
% Geometric Tools, LLC, (1999)&quot;
% http:\\www.geometrictools.com/Documentation/DistancePoint3Triangle3.pdf
%
%        ^t
%  \     |
%   \reg2|
%    \   |
%     \  |
%      \ |
%       \|
%        *P2
%        |\
%        | \
%  reg3  |  \ reg1
%        |   \
%        |reg0\ 
%        |     \ 
%        |      \ P1
% -------*-------*-------&gt;s
%        |P0      \ 
%  reg4  | reg5    \ reg6


%% Do some error checking
if nargin&lt;2
  error('pointTriangleDistance: too few arguments see help.');
end
P = P(:)';
if size(P,2)~=3
  error('pointTriangleDistance: P needs to be of length 3.');
end

if size(TRI)~=[3 3]
  error('pointTriangleDistance: TRI needs to be of size 3x3.');
end

% ToDo: check for colinearity and/or too small triangles.


% rewrite triangle in normal form
B = TRI(1,:);
E0 = TRI(2,:)-B;
%E0 = E0/sqrt(sum(E0.^2)); %normalize vector
E1 = TRI(3,:)-B;
%E1 = E1/sqrt(sum(E1.^2)); %normalize vector


D = B - P;
a = dot(E0,E0);
b = dot(E0,E1);
c = dot(E1,E1);
d = dot(E0,D);
e = dot(E1,D);
f = dot(D,D);

det = a*c - b*b; % do we have to use abs here?
s   = b*e - c*d;
t   = b*d - a*e;

% Terible tree of conditionals to determine in which region of the diagram
% shown above the projection of the point into the triangle-plane lies.
if (s+t) &lt;= det
  if s &lt; 0
    if t &lt; 0
      %region4
      if (d &lt; 0)
        t = 0;
        if (-d &gt;= a)
          s = 1;
          sqrDistance = a + 2*d + f;
        else
          s = -d/a;
          sqrDistance = d*s + f;
        end
      else
        s = 0;
        if (e &gt;= 0)
          t = 0;
          sqrDistance = f;
        else
          if (-e &gt;= c)
            t = 1;
            sqrDistance = c + 2*e + f;
          else
            t = -e/c;
            sqrDistance = e*t + f;
          end
        end
      end %of region 4
    else
      % region 3
      s = 0;
      if e &gt;= 0
        t = 0;
        sqrDistance = f;
      else
        if -e &gt;= c
          t = 1;
          sqrDistance = c + 2*e +f;
        else
          t = -e/c;
          sqrDistance = e*t + f;
        end
      end
    end %of region 3 
  else
    if t &lt; 0
      % region 5
      t = 0;
      if d &gt;= 0
        s = 0;
        sqrDistance = f;
      else
        if -d &gt;= a
          s = 1;
          sqrDistance = a + 2*d + f;% GF 20101013 fixed typo d*s -&gt;2*d
        else
          s = -d/a;
          sqrDistance = d*s + f;
        end
      end
    else
      % region 0
      invDet = 1/det;
      s = s*invDet;
      t = t*invDet;
      sqrDistance = s*(a*s + b*t + 2*d) ...
                  + t*(b*s + c*t + 2*e) + f;
    end
  end
else
  if s &lt; 0
    % region 2
    tmp0 = b + d;
    tmp1 = c + e;
    if tmp1 &gt; tmp0 % minimum on edge s+t=1
      numer = tmp1 - tmp0;
      denom = a - 2*b + c;
      if numer &gt;= denom
        s = 1;
        t = 0;
        sqrDistance = a + 2*d + f; % GF 20101014 fixed typo 2*b -&gt; 2*d
      else
        s = numer/denom;
        t = 1-s;
        sqrDistance = s*(a*s + b*t + 2*d) ...
                    + t*(b*s + c*t + 2*e) + f;
      end
    else          % minimum on edge s=0
      s = 0;
      if tmp1 &lt;= 0
        t = 1;
        sqrDistance = c + 2*e + f;
      else
        if e &gt;= 0
          t = 0;
          sqrDistance = f;
        else
          t = -e/c;
          sqrDistance = e*t + f;
        end
      end
    end %of region 2
  else
    if t &lt; 0
      %region6 
      tmp0 = b + e;
      tmp1 = a + d;
      if (tmp1 &gt; tmp0)
        numer = tmp1 - tmp0;
        denom = a-2*b+c;
        if (numer &gt;= denom)
          t = 1;
          s = 0;
          sqrDistance = c + 2*e + f;
        else
          t = numer/denom;
          s = 1 - t;
          sqrDistance = s*(a*s + b*t + 2*d) ...
                      + t*(b*s + c*t + 2*e) + f;
        end
      else  
        t = 0;
        if (tmp1 &lt;= 0)
            s = 1;
            sqrDistance = a + 2*d + f;
        else
          if (d &gt;= 0)
              s = 0;
              sqrDistance = f;
          else
              s = -d/a;
              sqrDistance = d*s + f;
          end
        end
      end
      %end region 6
    else
      % region 1
      numer = c + e - b - d;
      if numer &lt;= 0
        s = 0;
        t = 1;
        sqrDistance = c + 2*e + f;
      else
        denom = a - 2*b + c;
        if numer &gt;= denom
          s = 1;
          t = 0;
          sqrDistance = a + 2*d + f;
        else
          s = numer/denom;
          t = 1-s;
          sqrDistance = s*(a*s + b*t + 2*d) ...
                      + t*(b*s + c*t + 2*e) + f;
        end
      end %of region 1
    end
  end
end

% account for numerical round-off error
if (sqrDistance &lt; 0)
  sqrDistance = 0;
end

dist = sqrt(sqrDistance);

if nargout&gt;1
  PP0 = B + s*E0 + t*E1;
end</pre></div>
            </td>
          </tr>
        </table>



      </td>
    </tr>

  </table>


<p id="contactus"><a href="/company/feedback/">Contact us</a></p>

      
</div>
<div class="clearboth">&nbsp;</div>
</div>
</div>
<!-- footer.html -->
<!-- START OF FOOTER -->

<div id="mlc-footer">
  <script type="text/javascript">
function clickDynamic(obj, target_url, tracking_code) {
	var pos=target_url.indexOf("?");
	if (pos<=0) { 
		var linkComponents = target_url + tracking_code;
		obj.href=linkComponents;
	} 
}
</script>
  <div class="wrapper">
    <div>
      <ul id="matlabcentral">
        <li class="copyright first">&copy; 1994-2013 The MathWorks, Inc.</li>
        <li class="first"><a href="/help.html" title="Site Help">Site Help</a></li>
        <li><a href="/company/aboutus/policies_statements/patents.html" title="patents" rel="nofollow">Patents</a></li>
        <li><a href="/company/aboutus/policies_statements/trademarks.html" title="trademarks" rel="nofollow">Trademarks</a></li>
        <li><a href="/company/aboutus/policies_statements/" title="privacy policy" rel="nofollow">Privacy Policy</a></li>
        <li><a href="/company/aboutus/policies_statements/piracy.html" title="preventing piracy" rel="nofollow">Preventing Piracy</a></li>
        <li class="last"><a href="/matlabcentral/termsofuse.html" title="Terms of Use" rel="nofollow">Terms of Use</a></li>
        <li class="icon"><a href="/company/rss/" title="RSS" class="rssfeed" rel="nofollow"><span class="text">RSS</span></a></li>
        <li class="icon"><a href="/programs/bounce_hub_generic.html?s_tid=mlc_fbk&url=https://plus.google.com/117177960465154322866?prsrc=3" title="Google+" class="google" rel="nofollow" target="_blank"><span class="text">Google+</span></a></li>
        <li class="icon"><a href="/programs/bounce_hub_generic.html?s_tid=mlc_fbk&url=http://www.facebook.com/MATLAB" title="Facebook" class="facebook" rel="nofollow" target="_blank"><span class="text">Facebook</span></a></li>
        		<li class="last icon"><a href="/programs/bounce_hub_generic.html?s_tid=mlc_twt&url=http://www.twitter.com/MATLAB" title="Twitter" class="twitter" rel="nofollow" target="_blank"><span class="text">Twitter</span></a></li>        
        
      </ul>
      <ul id="mathworks">
        <li class="first sectionhead">Featured MathWorks.com Topics:</li>
        <li class="first"><a href="/products/new_products/latest_features.html" onclick="clickDynamic(this, this.href, '?s_cid=MLC_new')">New Products</a></li>
        <li><a href="/support/" title="support" onclick="clickDynamic(this, this.href, '?s_cid=MLC_support')">Support</a></li>
        <li><a href="/help" title="documentation" onclick="clickDynamic(this, this.href, '?s_cid=MLC_doc')">Documentation</a></li>
        <li><a href="/services/training/" title="training" onclick="clickDynamic(this, this.href, '?s_cid=MLC_training')">Training</a></li>
        <li><a href="/company/events/webinars/" title="Webinars" onclick="clickDynamic(this, this.href, '?s_cid=MLC_webinars')">Webinars</a></li>
        <li><a href="/company/newsletters/" title="newsletters" onclick="clickDynamic(this, this.href, '?s_cid=MLC_newsletters')">Newsletters</a></li>
        <li><a href="/programs/trials/trial_request.html?prodcode=ML&s_cid=MLC_trials" title="MATLAB Trials">MATLAB Trials</a></li>
        
        		<li class="last"><a href="/company/jobs/opportunities/index_en_US.html" title="Careers" onclick="clickDynamic(this, this.href, '?s_cid=MLC_careers')">Careers</a></li>
                 
      </ul>
    </div>
  </div>
</div>
<!-- END OF FOOTER -->


      
      
<!-- SiteCatalyst code version: H.24.4.
Copyright 1996-2012 Adobe, Inc. All Rights Reserved
More info available at http://www.omniture.com -->
<script language="JavaScript" type="text/javascript" src="/scripts/omniture/s_code.js"></script>


<script language="JavaScript" type="text/javascript">



<!--
/************* DO NOT ALTER ANYTHING BELOW THIS LINE ! **************/
var s_code=s.t();if(s_code)document.write(s_code)//--></script>
<script language="JavaScript" type="text/javascript"><!--
if(navigator.appVersion.indexOf('MSIE')>=0)document.write(unescape('%3C')+'\!-'+'-')
//--></script>
<!--/DO NOT REMOVE/-->
<!-- End SiteCatalyst code version: H.24.4. -->    </body>
</html>
