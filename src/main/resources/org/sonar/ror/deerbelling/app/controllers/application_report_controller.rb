require 'base64'
require 'fileutils'

class ApplicationReportController < ApplicationController

  def getApplicationPdf
    project = Project.by_key(params[:resource])
    send_file Rails.root.join('application-report', 'application_report_' + project.key.gsub(':', '-') + '.pdf'),  :type => 'application/pdf', :filename => project.name + '_application_report.pdf',:disposition => 'attachment'
  end
  
  def postReport
    uploaded = params[:upload]
    filename = uploaded.original_filename
    FileUtils::mkdir_p Rails.root.join('application-report') unless File.exists?(Rails.root.join('application-report'))
    File.open(Rails.root.join('application-report', filename), 'wb') do |file|
      file.write(uploaded.read)
    end
    render :nothing => true, :status => 200
  end

end
