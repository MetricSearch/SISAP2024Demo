$wnd.showcase.runAsyncCallback11("function qEb(){}\nfunction sEb(){}\nfunction lEb(a,b){a.b=b}\nfunction mEb(a){if(a==bEb){return true}zz();return a==eEb}\nfunction nEb(a){if(a==aEb){return true}zz();return a==_Db}\nfunction rEb(a){this.b=(WFb(),RFb).a;this.e=(_Fb(),$Fb).a;this.a=a}\nfunction jEb(a,b){var c;c=nC(a.fb,152);c.b=b.a;!!c.d&&dzb(c.d,b)}\nfunction kEb(a,b){var c;c=nC(a.fb,152);c.e=b.a;!!c.d&&fzb(c.d,b)}\nfunction fEb(){fEb=EX;$Db=new qEb;bEb=new qEb;aEb=new qEb;_Db=new qEb;cEb=new qEb;dEb=new qEb;eEb=new qEb}\nfunction oEb(){fEb();hzb.call(this);this.b=(WFb(),RFb);this.c=(_Fb(),$Fb);(Vvb(),this.e)[uac]=0;this.e[vac]=0}\nfunction gEb(a,b,c){var d;if(c==$Db){if(b==a.a){return}else if(a.a){throw $W(new jXb('Only one CENTER widget may be added'))}}Sh(b);eQb(a.j,b);c==$Db&&(a.a=b);d=new rEb(c);b.fb=d;jEb(b,a.b);kEb(b,a.c);iEb(a);Uh(b,a)}\nfunction hEb(a){var b,c,d,e,f,g,h;NPb((Vvb(),a.hb),'',Wbc);g=new S2b;h=new oQb(a.j);while(h.b<h.c.c){b=mQb(h);f=nC(b.fb,152).a;d=nC(b$b(i3b(g.d,f)),84);c=!d?1:d.a;e=f==cEb?'north'+c:f==dEb?'south'+c:f==eEb?'west'+c:f==_Db?'east'+c:f==bEb?'linestart'+c:f==aEb?'lineend'+c:Z8b;NPb(Vo(b.hb),Wbc,e);n$b(g,f,wXb(c+1))}}\nfunction iEb(a){var b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r;b=(Vvb(),a.d);while(zxb(b)>0){Bo(b,yxb(b,0))}o=1;e=1;for(i=new oQb(a.j);i.b<i.c.c;){d=mQb(i);f=nC(d.fb,152).a;f==cEb||f==dEb?++o:(f==_Db||f==eEb||f==bEb||f==aEb)&&++e}p=wB(uR,t6b,262,o,0,1);for(g=0;g<o;++g){p[g]=new sEb;p[g].b=$doc.createElement(sac);xo(b,awb(p[g].b))}k=0;l=e-1;m=0;q=o-1;c=null;for(h=new oQb(a.j);h.b<h.c.c;){d=mQb(h);j=nC(d.fb,152);r=$doc.createElement(tac);j.d=r;j.d[gac]=j.b;j.d.style[hac]=j.e;j.d[L6b]=j.f;j.d[K6b]=j.c;if(j.a==cEb){Yvb(p[m].b,r,p[m].a);xo(r,awb(d.hb));r[kbc]=l-k+1;++m}else if(j.a==dEb){Yvb(p[q].b,r,p[q].a);xo(r,awb(d.hb));r[kbc]=l-k+1;--q}else if(j.a==$Db){c=r}else if(mEb(j.a)){n=p[m];Yvb(n.b,r,n.a++);xo(r,awb(d.hb));r[Xbc]=q-m+1;++k}else if(nEb(j.a)){n=p[m];Yvb(n.b,r,n.a);xo(r,awb(d.hb));r[Xbc]=q-m+1;--l}}if(a.a){n=p[m];Yvb(n.b,c,n.a);xo(c,awb(fh(a.a)))}}\nvar Wbc='cwDockPanel';DX(415,1,Y8b);_.Ec=function reb(){var a,b,c;TZ(this.a,(a=new oEb,(Vvb(),a.hb).className='cw-DockPanel',a.e[uac]=4,lEb(a,(WFb(),QFb)),gEb(a,new NCb('This is the first north component'),(fEb(),cEb)),gEb(a,new NCb('This is the first south component'),dEb),gEb(a,new NCb('This is the east component'),_Db),gEb(a,new NCb('This is the west component'),eEb),gEb(a,new NCb('This is the second north component'),cEb),gEb(a,new NCb('This is the second south component'),dEb),b=new NCb(\"This is a <code>ScrollPanel<\\/code> contained at the center of a <code>DockPanel<\\/code>.  By putting some fairly large contents in the middle and setting its size explicitly, it becomes a scrollable area within the page, but without requiring the use of an IFRAME.<br><br>Here's quite a bit more meaningless text that will serve primarily to make this thing scroll off the bottom of its visible area.  Otherwise, you might have to make it really, really small in order to see the nifty scroll bars!\"),c=new gAb(b),c.hb.style[L6b]='400px',c.hb.style[K6b]='100px',gEb(a,c,$Db),hEb(a),a))};DX(872,254,Q6b,oEb);_.gc=function pEb(a){var b;b=_xb(this,a);if(b){a==this.a&&(this.a=null);iEb(this)}return b};var $Db,_Db,aEb,bEb,cEb,dEb,eEb;var vR=PWb(O6b,'DockPanel',872);DX(151,1,{},qEb);var sR=PWb(O6b,'DockPanel/DockLayoutConstant',151);DX(152,1,{152:1},rEb);_.c='';_.f='';var tR=PWb(O6b,'DockPanel/LayoutData',152);DX(262,1,{262:1},sEb);_.a=0;var uR=PWb(O6b,'DockPanel/TmpRow',262);$5b(El)(11);\n//# sourceURL=showcase-11.js\n")
