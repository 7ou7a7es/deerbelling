require 'base64'
require 'fileutils'

class TasksReportController < ApplicationController

  def getTasks
    project = Project.by_key(params[:resource])
    send_file Rails.root.join('tasks-report', 'tasks_report_' + project.key.gsub(':', '-') + '.xlsx'),  :type => 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', :filename => project.name + '_tasks_list.xlsx',:disposition => 'attachment'
  end

  def postReport
    uploaded = params[:upload]
    filename = uploaded.original_filename
    FileUtils::mkdir_p Rails.root.join('tasks-report') unless File.exists?(Rails.root.join('tasks-report'))
    File.open(Rails.root.join('tasks-report', filename), 'wb') do |file|
      file.write(uploaded.read)
    end
    render :nothing => true, :status => 200
  end

end
