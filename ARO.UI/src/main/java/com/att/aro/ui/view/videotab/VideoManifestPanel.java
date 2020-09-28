/*
*  Copyright 2017 AT&T
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.att.aro.ui.view.videotab;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.att.aro.core.pojo.AROTraceData;
import com.att.aro.core.videoanalysis.pojo.VideoEvent;
import com.att.aro.core.videoanalysis.pojo.VideoStream;
import com.att.aro.ui.commonui.AROUIManager;
import com.att.aro.ui.commonui.IARODiagnosticsOverviewRoute;
import com.att.aro.ui.commonui.TabPanelJScrollPane;
import com.att.aro.ui.view.MainFrame;

public class VideoManifestPanel extends TabPanelJScrollPane{

	private static final long serialVersionUID = 1L;
	private JPanel videoManifestPanel;
	private List<SegmentPanel> segmentTableList = new ArrayList<>();
	private IARODiagnosticsOverviewRoute overviewRoute;
	private JPanel manifestPanel;
	private MainFrame aroView;
	private VideoEvent videoEvent;

	public VideoManifestPanel(IARODiagnosticsOverviewRoute overviewRoute, MainFrame aroView) {

		this.overviewRoute = overviewRoute;
		this.aroView = aroView;
		videoManifestPanel = new JPanel();
		videoManifestPanel.setLayout(new BoxLayout(videoManifestPanel, BoxLayout.PAGE_AXIS));
		videoManifestPanel.setBackground(UIManager.getColor(AROUIManager.PAGE_BACKGROUND_KEY));

		manifestPanel = getManifestPanel();
		videoManifestPanel.add(manifestPanel);
		setViewportView(videoManifestPanel);
	}

	private JPanel getManifestPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBackground(UIManager.getColor(AROUIManager.PAGE_BACKGROUND_KEY));
		for (SegmentPanel segmentTable : segmentTableList) {
			panel.add(segmentTable);
		}
		return panel;
	}
	
	@Override
	public void refresh(AROTraceData analyzerResult) {
		segmentTableList.clear();
		if (analyzerResult != null && analyzerResult.getAnalyzerResult() != null && analyzerResult.getAnalyzerResult().getStreamingVideoData() != null) {
			for (VideoStream videoStream : analyzerResult.getAnalyzerResult().getStreamingVideoData().getVideoStreamMap().values()) {
				if (videoStream != null && videoStream.getVideoEventList() != null
						&& !videoStream.getVideoEventList().isEmpty()) {
					//boolean isplayRequestedTimeSet = videoStream.getPlayRequestedTime() != null ? true : false;
					SegmentPanel component = new SegmentPanel(videoStream, this.overviewRoute, analyzerResult, aroView, this);
					component.setVisible(false);
					segmentTableList.add(component);
				}
			}
		}
		videoManifestPanel.remove(manifestPanel);
		manifestPanel = getManifestPanel();
		videoManifestPanel.add(manifestPanel);
		videoManifestPanel.updateUI();
	}
	
	public void reload(AROTraceData analyzerResult, VideoEvent videoEvent) {
		this.videoEvent=videoEvent;
		refresh(analyzerResult);
	}
	
	public void refreshLocal(AROTraceData analyzerResult) {
		for (SegmentPanel segmentTable : segmentTableList) {
			segmentTable.updateTitleButton(analyzerResult);
		}
	}
	
	@Override
	public JPanel layoutDataPanel() {
		return null;
	}
	
	@Override
	public void setScrollLocationMap() {
	}
	 
	
	public List<SegmentPanel> getSegmentTableList() {
	        return segmentTableList;
	}
}
